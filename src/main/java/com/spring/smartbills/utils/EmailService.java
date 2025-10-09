package com.spring.smartbills.utils;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.spring.smartbills.entity.Metadata;
import com.spring.smartbills.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTML;
import java.io.IOException;

@Slf4j
@Service
public class EmailService {

//    @Autowired
//    private JavaMailSender mailSender;

    @Value("${spring.sendgrid.api-key}")
    private String sendgridApiKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Todo : Add this feature in SmartBills version 2.
//    public void sendBillReminderEmail(Metadata bill, String to) {
//        logger.info("Sending bill reminder email to {} for bill {}", to, bill.getTitle());
//
//        Email from = new Email("Raunakkumar.singh05@gmail.com");
//        String subject = "Bill Reminder: " + bill.getTitle();
//
//        String htmlContent = "<html><body>" +
//                "<h2>Bill Payment Reminder</h2>" +
//                "<p>Hello,</p>" +
//                "<p>This is a friendly reminder that your bill <strong>'" + bill.getTitle() + "'</strong> is due soon.</p>" +
//                "<h3>Bill Details:</h3>" +
//                "<ul>" +
//                "<li><strong>Title:</strong> " + bill.getTitle() + "</li>" +
//                "<li><strong>Due Date:</strong> " + bill.getDuedate() + "</li>" +
//                "</ul>" +
//                "<p>Please ensure payment is made on time to avoid any late fees or service disruptions.</p>" +
//                "<p>You can manage this bill by logging into your SmartBills account.</p>" +
//                "<br/><p>Best regards,<br/>SmartBills Team</p>" +
//                "</body></html>";
//        Content content = new Content("text/html", htmlContent);
//
//        Email receiver = new Email(to);
//        Mail mail = new Mail(from, subject, receiver, content);
//
//        SendGrid sg = new SendGrid(sendgridApiKey);
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//
//            Response response = sg.api(request);
//            logger.info("SendGrid response status: {}", response.getStatusCode());
//            logger.debug("SendGrid response headers: {}", response.getHeaders());
//            logger.debug("SendGrid response body: {}", response.getBody());
//
//            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
//                logger.info("Bill reminder email sent successfully to {} for bill {}", to, bill.getTitle());
//            } else {
//                logger.warn("SendGrid returned non-success status code: {} for email to {}", response.getStatusCode(), to);
//            }
//        } catch(IOException ex) {
//            logger.error("Error sending bill reminder email to {}: {}", to, ex.getMessage(), ex);
//            throw new RuntimeException("Error sending bill reminder email", ex);
//        }
//    }

    public void sendWelcomeEmail(String username,String to) {
        logger.info("Sending welcome email to {}", to);

        Email from = new Email("Raunakkumar.singh05@gmail.com");
        String subject = "Welcome To SmartBills";

        String htmlContent = "<html><body>" +
                "<h2>Welcome to SmartBills!</h2>" +
                "<p>Hello " + username + ",</p>" +
                "<p>Thank you for registering with SmartBills. Your account has been successfully created!</p>" +
                "<p>You can now start managing your bills and invoices efficiently with our platform.</p>" +
                "<h3>Getting Started:</h3>" +
                "<ul>" +
                "<li>Upload and organize your bills</li>" +
                "<li>Track your expenses</li>" +
                "<li>Categorize your bills for better management</li>" +
                "</ul>" +
                "<p>If you have any questions or need assistance, feel free to reach out to our support team.</p>" +
                "<br/><p>Best regards,<br/>SmartBills Team</p>" +
                "</body></html>";
        Content content = new Content("text/html", htmlContent);

        Email receiver = new Email(to);
        Mail mail = new Mail(from, subject, receiver, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            logger.info("SendGrid response status: {}", response.getStatusCode());
            logger.debug("SendGrid response headers: {}", response.getHeaders());
            logger.debug("SendGrid response body: {}", response.getBody());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("Welcome Mail sent successfully to {}", to);
            }else {
                logger.warn("SendGrid returned non-success status code: {} for email to {}", response.getStatusCode(), to);
            }
        } catch(IOException ex) {
                throw new RuntimeException("error sending mail", ex);
        }
    }

    public void sendPasswordResetEmail(String to, String token) {
        logger.info("Sending password reset email to {}", to);

        Email from = new Email("Raunakkumar.singh05@gmail.com");
        String subject = "Password Reset Request";
        Email receiver = new Email(to);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        // HTML content for clickable link
        String htmlContent = "<html><body>" +
                "<h2>Password Reset Request</h2>" +
                "<p>To reset your password, click the link below:</p>" +
                "<p><a href=\"" + resetLink + "\">Reset Password</a></p>" +
                "<p>Or copy and paste this URL into your browser:</p>" +
                "<p>" + resetLink + "</p>" +
                "<p>This link will expire in 24 hours.</p>" +
                "<p>If you did not request this password reset, please ignore this email.</p>" +
                "<br/><p>Best regards,<br/>SmartBills Team</p>" +
                "</body></html>";

        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, receiver, content);
        SendGrid sg = new SendGrid(sendgridApiKey);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            logger.info("SendGrid response status: {}", response.getStatusCode());
            logger.debug("SendGrid response headers: {}", response.getHeaders());
            logger.debug("SendGrid response body: {}", response.getBody());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("Password reset email successfully sent to {}", to);
            } else {
                logger.warn("SendGrid returned non-success status code: {} for email to {}", response.getStatusCode(), to);
            }

        } catch (IOException ex) {
            logger.error("Error sending password reset email to {}: {}", to, ex.getMessage(), ex);
            throw new RuntimeException("Failed to send password reset email", ex);
        }
    }
}
