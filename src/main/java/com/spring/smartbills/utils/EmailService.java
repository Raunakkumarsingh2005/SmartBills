package com.spring.smartbills.utils;

import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n\n" +
                "http://localhost:3000/reset-password?token=" + token + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you did not request this password reset, please ignore this email.");
        message.setFrom("raunakkumar.singh05@gmail.com");
        
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String username,String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email );
            message.setSubject("Welcome to SmartBills!");
            message.setText("Hello " + username + ",\n\n" +
                    "Welcome to SmartBills! Your account has been successfully created.\n\n" +
                    "You can now start managing your invoices and bills.\n\n" +
                    "Best regards,\n" +
                    "The SmartBills Team");
            message.setFrom("raunakkumar.singh05@gmail.com");

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending welcome email: " + e.getStackTrace());
        }
    }

    public void sendBillReminderEmail(Metadata bill, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setTo(user.getEmail());
        message.setSubject("Bill Reminder: " + bill.getTitle());
        message.setText("Hello,\n\n" +
                "This is a reminder that your bill '" + bill.getTitle() + "' is due tomorrow.\n\n" +
                "Category: " + bill.getCategory() + "\n" +
                "Due Date: " + bill.getDuedate() + "\n\n" +
                "Please ensure payment is made on time.\n\n" +
                "Best regards,\n" +
                "SmartBills Team");
        message.setFrom("raunakkumar.singh05@gmail.com");
        
        mailSender.send(message);
    }
}
