package com.spring.smartbills.repository;

import com.spring.smartbills.entity.Metadata;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Metadata, Long> {
    List<Metadata> findByDuedateAndReminderSent(LocalDate duedate, boolean reminderSent);
}
