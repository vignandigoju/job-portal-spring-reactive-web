package com.example.job_portal.repository;

import com.example.job_portal.entity.JobApplication;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface JobApplicationRepository extends ReactiveMongoRepository<JobApplication, String> {
    Flux<JobApplication> findByJobIdAndActiveTrue(String jobId);
}
