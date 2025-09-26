package com.spring.smartbills.service;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface BillService {
    public ResponseEntity<ResponseDto> uploadBill(MultipartFile file, BillUploadDto metadata);

    String uniqueFileName(MultipartFile file);

    ResponseEntity<List<Metadata>> getAllBills();

    ResponseEntity<ResponseDto> deleteBillById(Long id);

    ResponseEntity<?> downloadBillById(Long id);

    ResponseEntity<?> previewBillById(Long billId);

    ResponseEntity<?> searchBillByTitle(String title);
}
