package com.example.job_portal.service;

import com.example.job_portal.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.util.Map;

public class JobApplicationListener {
    @Autowired
    private EmailService emailService;

    @JmsListener(destination = "jobApplicationQueue")
    public void processJobApplication(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> messageMap = mapper.readValue(message, Map.class);

            emailService.sendEmail(
                    messageMap.get("applicantEmail"),
                    "Job Application Received",
                    "Thank you for applying to " + messageMap.get("jobTitle")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}