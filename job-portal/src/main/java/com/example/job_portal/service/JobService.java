package com.example.job_portal.service;

import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.JobApplication;
import com.example.job_portal.repository.JobApplicationRepository;
import com.example.job_portal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;

    public Mono<Job> createJob(Job job) {
        return jobRepository.save(job);
    }

    public Mono<JobApplication> applyForJob(String jobId, JobApplication application) {
        return jobRepository.findById(jobId)
                .switchIfEmpty(Mono.error(new RuntimeException("Job not found")))
                .flatMap(job -> {
                    application.setId(job.getId());
                    application.setAppliedDate(LocalDateTime.now());
                    return applicationRepository.save(application)
                            .doOnSuccess(savedApplication -> {
                                System.out.println("Application saved: " + savedApplication);
                                Map<String, String> message = Map.of(
                                        "applicantEmail", application.getApplicantEmail(),
                                        "jobTitle", job.getTitle()
                                );
                                System.out.println("Notification prepared: " + message);
                                emailService.sendEmail(
                                        application.getApplicantEmail(),
                                        "Job Applied Successfully!",
                                        "Your application for " + application.getJob().getTitle() + " has been applied successfully."
                                );
                                // Place email sending logic here
                            });
                });
    }

    public Flux<Job> filterJobs(String city, String skills, String level, String department) {
        return jobRepository.findByActiveTrueAndCityAndSkillsContainingAndLevelAndDepartment(
                city, skills, level, department);
    }

    public Mono<Job> updateJob(String jobId, Job jobDetails) {
        return jobRepository.findById(jobId)
                .switchIfEmpty(Mono.error(new RuntimeException("Job not found")))
                .flatMap(existingJob -> {
                    existingJob.setTitle(jobDetails.getTitle());
                    existingJob.setDescription(jobDetails.getDescription());
                    existingJob.setCity(jobDetails.getCity());
                    existingJob.setSkills(jobDetails.getSkills());
                    existingJob.setLevel(jobDetails.getLevel());
                    existingJob.setDepartment(jobDetails.getDepartment());
                    existingJob.setDepartment(jobDetails.getDepartment());
                    existingJob.setActive(jobDetails.isActive());
                    return jobRepository.save(existingJob);
                });
    }

    public Mono<Void> deleteJob(String jobId) {
        return jobRepository.findById(jobId)
                .switchIfEmpty(Mono.error(new RuntimeException("Job not found")))
                .flatMap(existingJob -> {
                    existingJob.setActive(false);
                    return jobRepository.save(existingJob);
                })
                .then();
    }

    public Mono<Void> withdrawApplication(String applicationId) {
        return applicationRepository.findById(applicationId)
                .switchIfEmpty(Mono.error(new RuntimeException("Application not found")))
                .flatMap(application -> {
                    application.setActive(false);
                    return applicationRepository.save(application)
                            .doOnSuccess(savedApplication -> emailService.sendEmail(
                                    application.getApplicantEmail(),
                                    "Job Application Withdrawn",
                                    "Your application for " + application.getJob() + " has been withdrawn."
                            ));
                })
                .then();
    }

    public Mono<JobApplication> updateApplication(String applicationId, JobApplication applicationDetails) {
        return applicationRepository.findById(applicationId)
                .switchIfEmpty(Mono.error(new RuntimeException("Application not found")))
                .flatMap(existingApplication -> {
                    existingApplication.setApplicantName(applicationDetails.getApplicantName());
                    existingApplication.setResume(applicationDetails.getResume());
                    return applicationRepository.save(existingApplication)
                            .doOnSuccess(savedApplication -> emailService.sendEmail(
                                    existingApplication.getApplicantEmail(),
                                    "Job Application Updated",
                                    "Your application for " + existingApplication.getJob() + " has been updated."
                            ));
                });
    }
}
