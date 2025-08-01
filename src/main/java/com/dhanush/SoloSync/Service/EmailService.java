package com.dhanush.SoloSync.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    //EMIALSENDER EMAIL
    private final String fromEmail="dhanusmileking@gmail.com";


    //EMAIL SENDING FUNCTION
    public void sendEmail(String toEmail,String body,String subject)
    {
        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }
        catch (Exception e)
        {
            throw new RuntimeException("EMAIL-ERROR : "+e.getMessage());
        }
    }




}
