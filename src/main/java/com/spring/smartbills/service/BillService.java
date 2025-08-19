package com.spring.smartbills.service;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Metadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BillService {
    public ResponseEntity<ResponseDto> uploadBill(MultipartFile file, BillUploadDto metadata);

    String uniqueFileName(MultipartFile file);

    ResponseEntity<List<Metadata>> getAllBills();

    ResponseEntity<ResponseDto> deleteBillById(Long id);
}
