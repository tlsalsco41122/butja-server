package com.dgsw.butja_server.domain.job_application.repository

import com.dgsw.butja_server.domain.job_application.domain.JobApplication
import org.springframework.data.jpa.repository.JpaRepository

interface JopApplicationRepository: JpaRepository<JobApplication, Long> {
    fun findAllByUserId(userId: Long): List<JobApplication>
}