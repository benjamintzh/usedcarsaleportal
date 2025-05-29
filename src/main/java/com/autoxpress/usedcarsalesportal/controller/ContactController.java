package com.autoxpress.usedcarsalesportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/contact/submit")
    public String handleContactFormSubmission(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            Model model) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("zhihongbtzh1234@gmail.com");
            mailMessage.setSubject("New Contact Form Submission: " + subject);
            mailMessage.setText(
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Subject: " + subject + "\n" +
                    "Message: " + message
            );
            mailMessage.setFrom("noreply@autoxpress.com"); // Configured in application.properties
            mailSender.send(mailMessage);
            model.addAttribute("successMessage", "Your message has been sent successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while sending your message. Please try again.");
        }
        return "contact";
    }
}