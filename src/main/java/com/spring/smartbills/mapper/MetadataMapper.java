package com.spring.smartbills.mapper;

import com.spring.smartbills.dtos.BillUploadDto;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

// Set the owner in your metadata
        metadata.setOwner(user);

        return metadata;
    }
}
