package com.spring.smartbills.repository;

import com.spring.smartbills.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Metadata, Long> {

}
