package com.spring.smartbills.utils;

import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.repository.MetadataRepository;
import com.spring.smartbills.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class CronJobs {
    @Autowired
    MetadataRepository metadataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendBillReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Metadata> billsDueTomorrow = metadataRepository.findByDuedateAndReminderSent(tomorrow, false);
        if (billsDueTomorrow.isEmpty()) {
            return;
        }

        for (Metadata bill : billsDueTomorrow) {
            Optional<User> user = userRepository.findByUserName(bill.getOwner().getUserName());
            // Send reminder email
            emailService.sendBillReminderEmail(bill, user.get().getEmail());
            bill.setReminderSent(true);
            metadataRepository.save(bill);
        }
    }
}
