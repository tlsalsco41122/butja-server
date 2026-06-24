package com.dgsw.butja_server.domain.job_application.repository

import com.dgsw.butja_server.domain.job_application.domain.JobApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface JobApplicationRepository: JpaRepository<JobApplication, Long> {
    @Query("select j from JobApplication j where j.user.id = :userId order by j.createdAt desc")
    fun findAllByUserId(@Param("userId") userId: Long): List<JobApplication>

    @Query("select j from JobApplication j where j.id = :id and j.user.id = :userId")
    fun findByIdAndUserId(@Param("id") id: Long, @Param("userId") userId: Long): Optional<JobApplication>
}
