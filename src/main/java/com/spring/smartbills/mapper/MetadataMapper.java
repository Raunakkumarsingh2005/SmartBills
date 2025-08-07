package com.spring.smartbills.mapper;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.entity.Metadata;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Data
public class MetadataMapper {
    @Value("${file.upload.url}")
    private String uploadUrl;

    public Metadata maptoMetadata(BillUploadDto billUploadDto) {
        Metadata metadata = new Metadata();
        metadata.setFileName(billUploadDto.getFileName());
        System.out.println(billUploadDto.getFileName());
        metadata.setCategory(billUploadDto.getCategory());
        System.out.println(metadata.getCategory());
        metadata.setFilePath(uploadUrl);
        System.out.println(metadata.getFilePath());
        return metadata;
    }
}
