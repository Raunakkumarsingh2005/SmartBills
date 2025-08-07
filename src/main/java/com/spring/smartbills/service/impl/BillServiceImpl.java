package com.spring.smartbills.service.impl;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Category;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.mapper.MetadataMapper;
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
import java.util.Optional;

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

    public BillServiceImpl(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> uploadFile(MultipartFile file, BillUploadDto metadata) {
        try {
            String uploadDir = uploadUrl;
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }

            // create filepath
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // write the actual file
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(ResponseContants.STATUS_400, "Unable to upload file"));
        }

        Metadata data = metadataMapper.maptoMetadata(metadata);
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
}
