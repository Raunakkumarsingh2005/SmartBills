package com.spring.smartbills.service.impl;

import com.spring.smartbills.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

interface MetadataRepository extends JpaRepository<Metadata, Long> {
    Metadata category(String category);
}
