package com.autoxpress.usedcarsalesportal.service;

import com.autoxpress.usedcarsalesportal.dto.AppointmentDto;
import com.autoxpress.usedcarsalesportal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String senderEmail = "zhihongbtzh1234@gmail.com"; // Must match spring.mail.username
    private final String senderName = "AutoXpress"; // Custom sender name

    public void sendBookingConfirmationEmail(User user, AppointmentDto appointmentDto, Long appointmentId) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(user.getEmail());
        helper.setSubject("AutoXpress - Test Drive Booking Confirmation");
        helper.setFrom(new InternetAddress(senderEmail, senderName));

        String htmlContent = """
            <html>
            <body style='font-family: Arial, sans-serif;'>
                <h2>Booking Confirmation</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your test drive appointment has been successfully booked with AutoXpress Used Car Sales.</p>
                <h3>Booking Details</h3>
                <ul>
                    <li><strong>Appointment ID:</strong> %d</li>
                    <li><strong>Car:</strong> %s %s</li>
                    <li><strong>Date and Time:</strong> %s</li>
                    <li><strong>Username:</strong> %s</li>
                    <li><strong>Email:</strong> %s</li>
                    %s
                </ul>
                <p>We look forward to seeing you! If you need to cancel or reschedule, please contact us at +60 12-405 2028.</p>
                <p>Best regards,<br>AutoXpress Team</p>
            </body>
            </html>
            """.formatted(
                user.getUsername(),
                appointmentId,
                appointmentDto.getCarManufacture(),
                appointmentDto.getCarModel(),
                appointmentDto.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                user.getUsername(),
                user.getEmail(),
                appointmentDto.getContactNumber() != null ? "<li><strong>Contact Number:</strong> " + appointmentDto.getContactNumber() + "</li>" : ""
            );

        helper.setText(htmlContent, true);
        mailSender.send(message);
        System.out.println("Booking confirmation email sent to: " + user.getEmail());
    }
}