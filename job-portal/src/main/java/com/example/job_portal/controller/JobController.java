package com.example.job_portal.controller;


import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.JobApplication;
import com.example.job_portal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public Mono<ResponseEntity<Job>> createJob(@RequestBody Job job) {
        return jobService.createJob(job)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{jobId}/apply")
    public Mono<ResponseEntity<JobApplication>> applyForJob(
            @PathVariable String jobId,
            @RequestBody JobApplication application) {
        return jobService.applyForJob(jobId, application)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/filter")
    public Flux<Job> filterJobs(
            @RequestParam String city,
            @RequestParam String skills,
            @RequestParam String level,
            @RequestParam String department) {
        return jobService.filterJobs(city, skills, level, department);
    }

    @PutMapping("/{jobId}")
    public Mono<ResponseEntity<Job>> updateJob(
            @PathVariable String jobId,
            @RequestBody Job jobDetails) {
        return jobService.updateJob(jobId, jobDetails)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{jobId}")
    public Mono<ResponseEntity<Void>> deleteJob(@PathVariable String jobId) {
        return jobService.deleteJob(jobId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/applications/{applicationId}")
    public Mono<ResponseEntity<Void>> withdrawApplication(@PathVariable String applicationId) {
        return jobService.withdrawApplication(applicationId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping("/applications/{applicationId}")
    public Mono<ResponseEntity<JobApplication>> updateApplication(
            @PathVariable String applicationId,
            @RequestBody JobApplication applicationDetails) {
        return jobService.updateApplication(applicationId, applicationDetails)
                .map(ResponseEntity::ok);
    }

}
