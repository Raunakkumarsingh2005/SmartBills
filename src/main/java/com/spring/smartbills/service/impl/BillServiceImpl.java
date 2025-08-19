package com.spring.smartbills.service.impl;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.mapper.MetadataMapper;
import com.spring.smartbills.repository.BillRepository;
import com.spring.smartbills.repository.CategoryRepository;
import com.spring.smartbills.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillServiceImpl implements BillService {
    @Value("${file.upload.url}")
    private String uploadUrl;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private  MetadataRepository metadataRepository;

    @Autowired
    private MetadataMapper metadataMapper;
    @Autowired
    private BillRepository billRepository;

    public BillServiceImpl(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> uploadBill(MultipartFile file, BillUploadDto metadata) {
        Path filePath;
        try {
            String uploadDir = uploadUrl;
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }

            // create filepath
            String fileName = uniqueFileName(file);
            filePath = Paths.get(uploadDir, fileName);

            // write the actual file
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(ResponseContants.STATUS_400, "Unable to upload file"));
        }

        Metadata data = metadataMapper.maptoMetadata(metadata);
        data.setUniqueFileName(uniqueFileName(file));
        data.setFilePath(String.valueOf(filePath));
        if (data == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(ResponseContants.STATUS_400, "Metadata not found"));
        }

        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(data.getCategory());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(ResponseContants.STATUS_400, "Category does not exist"));
        }


        metadataRepository.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201", ResponseContants.MESSAGE_200));
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
        List<Metadata> metadataList = metadataRepository.findAll();
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
            billRepository.deleteById(id);

            // Best practice: use 200 OK when returning a message body
            return ResponseEntity.ok(new ResponseDto("200", "Bill deleted successfully"));

        } catch (IOException e) {
            // Always log the error
            // logger.error("Could not delete bill with id {}", id, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Could not delete bill"));
        }
    }

}
