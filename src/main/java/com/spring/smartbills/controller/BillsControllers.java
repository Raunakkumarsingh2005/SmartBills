package com.spring.smartbills.controller;

import com.spring.smartbills.contants.ResponseContants;
import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.service.BillService;
import com.spring.smartbills.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
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
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("bill/upload")
    public ResponseEntity<ResponseDto> uploadBill(@RequestPart("file") MultipartFile file,@Validated @RequestPart("metadata") BillUploadDto metadata) {
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

    @GetMapping("/bill/download/{id}")
    public ResponseEntity<?> downloadBills(@PathVariable Long id) {
        return billsService.downloadBillById(id);
    }

    @GetMapping("/bill/preview/{id}")
    public ResponseEntity<?> previewBillById(@PathVariable Long id) {
        return billsService.previewBillById(id);
    }

    @GetMapping("/bill/search")
    public ResponseEntity<?> searchBillsByTitle(@RequestParam String title) {
        return billsService.searchBillByTitle(title);
    }
}
