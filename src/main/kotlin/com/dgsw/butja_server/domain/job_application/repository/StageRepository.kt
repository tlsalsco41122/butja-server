package com.dgsw.butja_server.domain.job_application.repository

import com.dgsw.butja_server.domain.job_application.domain.Stage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StageRepository: JpaRepository<Stage, Long> {
    fun findAllByJobApplicationIdOrderByOrderNumberAsc(jobApplicationId: Long): List<Stage>

    fun findByIdAndJobApplicationId(id: Long, jobApplicationId: Long): Optional<Stage>

    fun findFirstByJobApplicationIdAndCompletedFalseOrderByOrderNumberAsc(jobApplicationId: Long): Stage?
}
