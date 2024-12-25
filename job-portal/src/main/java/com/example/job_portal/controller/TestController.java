package com.example.job_portal.controller;

import com.example.job_portal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to, String subject, String body) {
        emailService.sendEmail(to, subject, body);
        return "Email sent to: " + to;
    }
}