package com.spring.smartbills.mapper;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.service.BillService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
@Data
public class MetadataMapper {
    @Value("${file.upload.url}")
    private String uploadUrl;

    public Metadata maptoMetadata(BillUploadDto billUploadDto) {
        Metadata metadata = new Metadata();
        metadata.setTitle(billUploadDto.getTitle());
        System.out.println(billUploadDto.getTitle());
        metadata.setCategory(billUploadDto.getCategory());
        System.out.println(metadata.getCategory());
        metadata.setDuedate(billUploadDto.getDuedate());

        return metadata;
    }
}
