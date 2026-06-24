package com.dgsw.butja_server.domain.job_application.service

import com.dgsw.butja_server.domain.auth.error.AuthErrorCode
import com.dgsw.butja_server.domain.job_application.domain.JobApplication
import com.dgsw.butja_server.domain.job_application.domain.Stage
import com.dgsw.butja_server.domain.job_application.error.JobApplicationErrorCode
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CreateApplicationReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateApplicationReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.JobApplicationDetailRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.JobApplicationRes
import com.dgsw.butja_server.domain.job_application.repository.JobApplicationRepository
import com.dgsw.butja_server.domain.job_application.repository.StageRepository
import com.dgsw.butja_server.global.exception.CustomException
import com.dgsw.butja_server.global.security.auth.UserSessionHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JobApplicationService(
    private val jobApplicationRepository: JobApplicationRepository,
    private val stageRepository: StageRepository,
    private val userSessionHolder: UserSessionHolder
) {
    @Transactional
    fun create(req: CreateApplicationReq): JobApplicationDetailRes {
        val currentUser = userSessionHolder.getCurrentUser()

        val jobApplication = JobApplication(
            user = currentUser,
            companyName = req.companyName,
            jobRole = req.jobRole,
            appliedDate = req.appliedDate
        ).apply {
            updateMemo(req.memo)
        }

        val savedApplication = jobApplicationRepository.save(jobApplication)
        return toDetailRes(savedApplication, emptyList())
    }

    @Transactional(readOnly = true)
    fun getList(): List<JobApplicationRes> {
        val currentUserId = getCurrentUserId()

        return jobApplicationRepository.findAllByUserId(currentUserId)
            .map { jobApplication ->
                val stages = getStages(jobApplication)
                toRes(jobApplication, stages)
            }
    }

    @Transactional(readOnly = true)
    fun getDetail(applicationId: Long): JobApplicationDetailRes {
        val jobApplication = getMyApplication(applicationId)
        val stages = getStages(jobApplication)

        return toDetailRes(jobApplication, stages)
    }

    @Transactional
    fun update(applicationId: Long, req: UpdateApplicationReq): JobApplicationDetailRes {
        val jobApplication = getMyApplication(applicationId)

        jobApplication.update(
            companyName = req.companyName,
            jobRole = req.jobRole,
            appliedDate = req.appliedDate,
            memo = req.memo
        )

        return toDetailRes(jobApplication, getStages(jobApplication))
    }

    @Transactional
    fun delete(applicationId: Long) {
        val jobApplication = getMyApplication(applicationId)
        val stages = getStages(jobApplication)

        stageRepository.deleteAll(stages)
        jobApplicationRepository.delete(jobApplication)
    }

    fun getMyApplication(applicationId: Long): JobApplication {
        val currentUserId = getCurrentUserId()

        return jobApplicationRepository.findByIdAndUserId(applicationId, currentUserId)
            .orElseThrow { CustomException(JobApplicationErrorCode.JOB_APPLICATION_NOT_FOUND) }
    }

    private fun getCurrentUserId(): Long {
        return userSessionHolder.getCurrentUser().id
            ?: throw CustomException(AuthErrorCode.INVALID_CREDENTIALS)
    }

    private fun getStages(jobApplication: JobApplication): List<Stage> {
        return stageRepository.findAllByJobApplicationIdOrderByOrderNumberAsc(jobApplication.requireId())
    }

    private fun toDetailRes(jobApplication: JobApplication, stages: List<Stage>): JobApplicationDetailRes {
        val currentStage = stages.findCurrentStage()
        val currentStageId = currentStage?.id

        return JobApplicationDetailRes(
            id = jobApplication.requireId(),
            companyName = jobApplication.companyName,
            jobRole = jobApplication.jobRole,
            appliedDate = jobApplication.appliedDate,
            memo = jobApplication.memo,
            currentStage = currentStage?.toRes(currentStageId),
            progress = stages.toProgressRes(),
            stages = stages.map { it.toRes(currentStageId) },
            createdAt = jobApplication.createdAt,
            updatedAt = jobApplication.updatedAt
        )
    }

    private fun toRes(jobApplication: JobApplication, stages: List<Stage>): JobApplicationRes {
        val currentStage = stages.findCurrentStage()
        val currentStageId = currentStage?.id

        return JobApplicationRes(
            id = jobApplication.requireId(),
            companyName = jobApplication.companyName,
            jobRole = jobApplication.jobRole,
            appliedDate = jobApplication.appliedDate,
            memo = jobApplication.memo,
            currentStage = currentStage?.toRes(currentStageId),
            progress = stages.toProgressRes(),
            createdAt = jobApplication.createdAt,
            updatedAt = jobApplication.updatedAt
        )
    }
}
