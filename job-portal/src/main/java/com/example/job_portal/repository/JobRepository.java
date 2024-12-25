package com.example.job_portal.repository;

import com.example.job_portal.entity.Job;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface JobRepository extends ReactiveMongoRepository<Job, String> {
    Flux<Job> findByActiveTrue();
    Flux<Job> findByActiveTrueAndCityAndSkillsContainingAndLevelAndDepartment(
            String city, String skills, String level, String department);
}
