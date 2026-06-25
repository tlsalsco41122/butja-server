package com.dgsw.butja_server.domain.job_application.service

import com.dgsw.butja_server.domain.job_application.domain.JobApplication
import com.dgsw.butja_server.domain.job_application.domain.Stage
import com.dgsw.butja_server.domain.job_application.domain.enums.StageStatus
import com.dgsw.butja_server.domain.job_application.error.JobApplicationErrorCode
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CompleteStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CreateStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateStageOrderReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.CurrentStageRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.ProgressRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.StageRes
import com.dgsw.butja_server.domain.job_application.repository.StageRepository
import com.dgsw.butja_server.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageService(
    private val jobApplicationService: JobApplicationService,
    private val stageRepository: StageRepository
) {
    @Transactional
    fun create(applicationId: Long, req: CreateStageReq): StageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val insertIndex = (req.orderNumber ?: stages.size).coerceIn(0, stages.size)

        val stage = Stage(
            jobApplication = jobApplication,
            name = req.name,
            orderNumber = insertIndex
        ).apply {
            update(name = null, scheduledAt = req.scheduledAt, memo = req.memo)
        }

        val reorderedStages = stages.toMutableList().apply {
            add(insertIndex, stage)
        }

        saveWithReorderedNumbers(reorderedStages)

        val hasInProgress = reorderedStages.any { it.status == StageStatus.IN_PROGRESS }
        if (!hasInProgress) {
            val firstStage = reorderedStages.minByOrNull { it.orderNumber }
            firstStage?.markInProgress()
            firstStage?.let { stageRepository.save(it) }
        }

        return stage.toRes(reorderedStages.findCurrentStage()?.id)
    }

    @Transactional(readOnly = true)
    fun getList(applicationId: Long): List<StageRes> {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val currentStageId = stages.findCurrentStage()?.id

        return stages.map { it.toRes(currentStageId) }
    }

    @Transactional(readOnly = true)
    fun getDetail(applicationId: Long, stageId: Long): StageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val currentStageId = stages.findCurrentStage()?.id
        val stage = stages.firstOrNull { it.id == stageId }
            ?: throw CustomException(JobApplicationErrorCode.STAGE_NOT_FOUND)

        return stage.toRes(currentStageId)
    }

    @Transactional
    fun update(applicationId: Long, stageId: Long, req: UpdateStageReq): StageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stage = getStage(jobApplication, stageId)

        stage.update(
            name = req.name,
            scheduledAt = req.scheduledAt,
            memo = req.memo
        )

        return stage.toRes(getStages(jobApplication).findCurrentStage()?.id)
    }

    @Transactional
    fun delete(applicationId: Long, stageId: Long) {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val stage = stages.firstOrNull { it.id == stageId }
            ?: throw CustomException(JobApplicationErrorCode.STAGE_NOT_FOUND)

        stageRepository.delete(stage)

        val remainingStages = stages.filter { it.id != stageId }
        saveWithReorderedNumbers(remainingStages)
    }

    @Transactional
    fun updateOrder(applicationId: Long, stageId: Long, req: UpdateStageOrderReq): List<StageRes> {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val stage = stages.firstOrNull { it.id == stageId }
            ?: throw CustomException(JobApplicationErrorCode.STAGE_NOT_FOUND)
        val targetIndex = req.orderNumber.coerceIn(0, stages.lastIndex)
        val reorderedStages = stages.filter { it.id != stageId }.toMutableList().apply {
            add(targetIndex, stage)
        }

        saveWithReorderedNumbers(reorderedStages)
        val currentStageId = reorderedStages.findCurrentStage()?.id

        return reorderedStages.map { it.toRes(currentStageId) }
    }

    @Transactional
    fun complete(applicationId: Long, stageId: Long, req: CompleteStageReq): StageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)

        // FAILED 상태인 지원서는 complete 불가
        if (stages.hasFailed()) {
            throw CustomException(JobApplicationErrorCode.ALREADY_FAILED_APPLICATION)
        }

        val stage = getStage(jobApplication, stageId)

        // IN_PROGRESS 상태인 전형만 완료 처리 가능
        if (stage.status != StageStatus.IN_PROGRESS) {
            throw CustomException(JobApplicationErrorCode.INVALID_STAGE_STATUS_TRANSITION)
        }

        stage.updateCompleted(req.completed)

        // 완료 처리 시 다음 PENDING 전형을 IN_PROGRESS로 변경
        if (req.completed) {
            val nextStage = stages
                .sortedBy { it.orderNumber }
                .firstOrNull { it.status == StageStatus.PENDING }
            nextStage?.markInProgress()
            nextStage?.let { stageRepository.save(it) }
        }

        return stage.toRes(getStages(jobApplication).findCurrentStage()?.id)
    }

    @Transactional
    fun fail(applicationId: Long, stageId: Long): StageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication)
        val stage = getStage(jobApplication, stageId)

        // IN_PROGRESS 상태인 전형만 불합격 처리 가능
        if (stage.status != StageStatus.IN_PROGRESS) {
            throw CustomException(JobApplicationErrorCode.INVALID_STAGE_STATUS_TRANSITION)
        }

        stage.markFailed()

        return stage.toRes(stages.findCurrentStage()?.id)
    }

    @Transactional(readOnly = true)
    fun getProgress(applicationId: Long): ProgressRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        return getStages(jobApplication).toProgressRes()
    }

    @Transactional(readOnly = true)
    fun getCurrentStage(applicationId: Long): CurrentStageRes {
        val jobApplication = jobApplicationService.getMyApplication(applicationId)
        val stages = getStages(jobApplication).sortedBy { it.orderNumber }
        val currentStage = stages
            .firstOrNull { it.status == StageStatus.IN_PROGRESS }
            ?.let { it.toRes(it.id) }

        return CurrentStageRes(currentStage)
    }

    private fun getStages(jobApplication: JobApplication): List<Stage> {
        return stageRepository.findAllByJobApplicationIdOrderByOrderNumberAsc(jobApplication.requireId())
    }

    private fun getStage(jobApplication: JobApplication, stageId: Long): Stage {
        return stageRepository.findByIdAndJobApplicationId(stageId, jobApplication.requireId())
            .orElseThrow { CustomException(JobApplicationErrorCode.STAGE_NOT_FOUND) }
    }

    private fun saveWithReorderedNumbers(stages: List<Stage>) {
        stages.forEachIndexed { index, stage ->
            stage.updateOrderNumber(index)
        }
        stageRepository.saveAll(stages)
    }
}