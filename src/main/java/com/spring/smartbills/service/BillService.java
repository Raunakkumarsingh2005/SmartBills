package com.spring.smartbills.service;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Metadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BillService {
    public ResponseEntity<ResponseDto> uploadFile(MultipartFile file, BillUploadDto metadata);
}
