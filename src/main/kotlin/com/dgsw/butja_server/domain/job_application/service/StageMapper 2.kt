package com.dgsw.butja_server.domain.job_application.service

import com.dgsw.butja_server.domain.job_application.domain.JobApplication
import com.dgsw.butja_server.domain.job_application.domain.Stage
import com.dgsw.butja_server.domain.job_application.domain.enums.StageStatus
import com.dgsw.butja_server.domain.job_application.error.JobApplicationErrorCode
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.ProgressRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.StageRes
import com.dgsw.butja_server.global.exception.CustomException

fun Stage.toRes(currentStageId: Long?): StageRes {
    return StageRes(
        id = requireId(),
        jobApplicationId = jobApplication.requireId(),
        name = name,
        orderNumber = orderNumber,
        completed = completed,
        status = resolveStatus(currentStageId),
        scheduledAt = scheduledAt,
        memo = memo
    )
}

fun List<Stage>.findCurrentStage(): Stage? {
    return sortedBy { it.orderNumber }.firstOrNull { !it.completed }
}

fun List<Stage>.toProgressRes(): ProgressRes {
    val totalCount = size
    val completedCount = count { it.completed }
    val progressRate = if (totalCount == 0) 0.0 else completedCount * 100.0 / totalCount

    return ProgressRes(
        totalCount = totalCount,
        completedCount = completedCount,
        progressRate = progressRate
    )
}

fun JobApplication.requireId(): Long {
    return id ?: throw CustomException(JobApplicationErrorCode.JOB_APPLICATION_NOT_FOUND)
}

fun Stage.requireId(): Long {
    return id ?: throw CustomException(JobApplicationErrorCode.STAGE_NOT_FOUND)
}

private fun Stage.resolveStatus(currentStageId: Long?): StageStatus {
    return when {
        completed -> StageStatus.COMPLETED
        id == currentStageId -> StageStatus.IN_PROGRESS
        else -> StageStatus.PENDING
    }
}
