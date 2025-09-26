package com.spring.smartbills.mapper;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.repository.UserRepository;
import com.spring.smartbills.service.BillService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
@Data
public class MetadataMapper {

    @Autowired
    private UserRepository userRepository;

    public Metadata maptoMetadata(BillUploadDto billUploadDto) {
        Metadata metadata = new Metadata();
        metadata.setTitle(billUploadDto.getTitle());
        System.out.println(billUploadDto.getTitle());
        metadata.setCategory(billUploadDto.getCategory());
        System.out.println(metadata.getCategory());
        metadata.setDuedate(billUploadDto.getDuedate());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

// Now get the User entity from your repository
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

// Set the owner in your metadata
        metadata.setOwner(user);

        return metadata;
    }
}
