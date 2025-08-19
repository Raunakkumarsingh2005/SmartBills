package com.spring.smartbills.controller;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.service.BillService;
import com.spring.smartbills.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("api/")
public class BillsControllers {
    @Autowired
    private BillService billsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("bill/upload")
    public ResponseEntity<ResponseDto> uploadBill(@RequestPart("file") MultipartFile file, @RequestPart("metadata") BillUploadDto metadata) {
        if (file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(ResponseContants.STATUS_400, ResponseContants.MESSAGE_400));
        }

        return billsService.uploadBill(file, metadata);
    }

    @GetMapping("/bill")
    public ResponseEntity<List<Metadata>> getAllBills() {
        return billsService.getAllBills();
    }

    @DeleteMapping("/bill")
    public ResponseEntity<ResponseDto> DeleteBillById(@RequestParam("id") Long id) {
        return billsService.deleteBillById(id);
    }
}
