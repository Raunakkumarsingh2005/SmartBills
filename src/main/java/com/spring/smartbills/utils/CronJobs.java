package com.spring.smartbills.utils;

import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CronJobs {
    @Autowired
    BillRepository billRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 54 14 * * *")
    // reminder will be given if reminderSent is set to false
    public void sendreminder() {
        List<Metadata> list = billRepository.findByDuedateAndReminderSent(LocalDate.now().plusDays(7), false);
        for (Metadata m : list) {
            try{
                //TODO add proper mail messages before sending it to production.
                //TODO here fetch the mail id's of users when user registration is enabled.
                emailService.sendMail("kumarsinghraunak27@gmail.com", "Reminder to pay " + m.getTitle(), "This is a gentle reminder to pay " + m.getTitle()
                        + " Last 7 days remaining to pay.");
                m.setReminderSent(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some error occured ");
            }
        }
    }
}
