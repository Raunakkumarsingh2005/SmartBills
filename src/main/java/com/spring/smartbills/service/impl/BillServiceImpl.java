package com.spring.smartbills.service.impl;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.BillSearch;
import com.spring.smartbills.entity.Category;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.mapper.MetadataMapper;
import com.spring.smartbills.repository.BillSearchRepository;
import com.spring.smartbills.repository.CategoryRepository;
import com.spring.smartbills.repository.MetadataRepository;
import com.spring.smartbills.repository.UserRepository;
import com.spring.smartbills.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillServiceImpl implements BillService {
    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);
    @Value("${file.upload.url}")
    private String uploadUrl;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private BillSearchRepository billSearchRepository;

    @Autowired
    private MetadataMapper metadataMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ResponseDto> uploadBill(MultipartFile file, BillUploadDto metadata) {

        if (file.isEmpty() || metadata == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("400", "File or metadata missing"));
        }

        // Map DTO to entity
        Metadata data = metadataMapper.maptoMetadata(metadata);

        // Validate category
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(data.getCategory());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(ResponseContants.STATUS_400, "Category does not exist"));
        }

        String fileName;
        Path filePath;

        try {
            System.out.println("1");
            // Ensure upload directory exists
            Path uploadPath = Paths.get(uploadUrl);
            if (!Files.exists(uploadPath)) {
                System.out.println("2");
                Files.createDirectories(uploadPath); // safer than createDirectory
            }

            // Create unique filename & path
            fileName = uniqueFileName(file);
            filePath = uploadPath.resolve(fileName);
            System.out.println("3");

            // Write file bytes
            Files.write(filePath, file.getBytes());
            System.out.println("4");
        } catch (IOException e) {
            System.out.println("5");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(ResponseContants.STATUS_400, "Unable to upload file"));
        }

        // Set file info in metadata
        data.setUniqueFileName(fileName);
        data.setFilePath(filePath.toString());



        // ⚠️ This null check is redundant → data is never null here
        // if (data == null) { ... } → should be removed

        // Save to SQL DB
        metadataRepository.save(data);

        // Save to Elasticsearch
        BillSearch doc = new BillSearch();
        doc.setId(data.getId().toString());
        doc.setCategory(data.getCategory());
        doc.setTitle(data.getTitle());
        doc.setVenderName(data.getVenderName());
        // doc.setDuedate(data.getDuedate()); // uncomment if field exists
        doc.setCreatedOn(data.getCreatedOn());

        billSearchRepository.save(doc);

        // Return response
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(ResponseContants.STATUS_200, ResponseContants.MESSAGE_200));
    }


    @Override
    public String uniqueFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = "";

        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        return fileName = UUID.randomUUID().toString() + extension;
    }

    @Override
    public ResponseEntity<List<Metadata>> getAllBills() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Metadata> metadataList = metadataRepository.findByOwner(user);
        return ResponseEntity.status(HttpStatus.OK).body(metadataList);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteBillById(Long id) {
        // 1. Check if metadata exists
        Optional<Metadata> billData = metadataRepository.findById(id);
        if (billData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Bill not found"));
        }

        try {
            // 2. Delete the associated file if it exists
            String filePath = billData.get().getFilePath();
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }

            // 3. Delete metadata record
            metadataRepository.deleteById(id);
            billSearchRepository.deleteById(id.toString());

            // Best practice: use 200 OK when returning a message body
            return ResponseEntity.ok(new ResponseDto("200", "Bill deleted successfully"));

        } catch (IOException e) {
            // Always log the error
            // logger.error("Could not delete bill with id {}", id, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Could not delete bill"));
        }
    }

    @Override
    public ResponseEntity<?> downloadBillById(Long billId) {
        Optional<Metadata> metadataOpt = metadataRepository.findById(billId);


        if (metadataOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Bill metadata not found"));
        }

        Metadata metadata = metadataOpt.get();
        Path filePath = Paths.get(metadata.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Bill file not found"));
        }

        try {
            UrlResource resource = new UrlResource(filePath.toUri());

            String originalTitle = metadata.getTitle();
            String storedFileName = metadata.getUniqueFileName();
            String fileExtension = storedFileName.substring(storedFileName.lastIndexOf("."));
            String downloadFileName = originalTitle + fileExtension;

            String contentType = null;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("Error loading file for billId {}: {}", billId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Error while preparing file for download"));
        }
    }

    @Override
    public ResponseEntity<?> previewBillById(Long billId) {
        Optional<Metadata> metadataOpt = metadataRepository.findById(billId);

        if (metadataOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Bill metadata not found"));
        }

        Metadata metadata = metadataOpt.get();
        Path filePath = Paths.get(metadata.getFilePath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "Bill file not found"));
        }

        try {
            UrlResource resource = new UrlResource(filePath.toUri());

            String originalTitle = metadata.getTitle();
            String storedFileName = metadata.getUniqueFileName();
            String fileExtension = storedFileName.substring(storedFileName.lastIndexOf("."));
            String downloadFileName = originalTitle + fileExtension;

            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + downloadFileName + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("Error loading file for billId {}: {}", billId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Error while preparing file for download"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> searchBillByTitle(String title) {
        if (title.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("400", "No title provided"));
        }

        List<BillSearch> bill = billSearchRepository.findByTitle(title);
        if (bill == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        return ResponseEntity.status(HttpStatus.OK).body(bill);
    }


}
