package com.spring.smartbills.repository;

import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MetadataRepository extends JpaRepository<Metadata,Long> {
    List<Metadata> findByDuedateAndReminderSent(LocalDate duedate, boolean reminderSent);
    List<Metadata> findByOwner(User user);
}
