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
        status = status,
        scheduledAt = scheduledAt,
        memo = memo
    )
}

fun List<Stage>.findCurrentStage(): Stage? {
    // FAILED가 있으면 FAILED 전형을 반환 (불합격 상태 표현)
    // 없으면 IN_PROGRESS → 없으면 첫 번째 PENDING 순으로 탐색
    return sortedBy { it.orderNumber }.let { sorted ->
        sorted.firstOrNull { it.status == StageStatus.FAILED }
            ?: sorted.firstOrNull { it.status == StageStatus.IN_PROGRESS }
            ?: sorted.firstOrNull { it.status == StageStatus.PENDING }
    }
}

fun List<Stage>.hasFailed(): Boolean {
    return any { it.status == StageStatus.FAILED }
}

fun List<Stage>.toProgressRes(): ProgressRes {
    val totalCount = size
    val completedCount = count { it.status == StageStatus.COMPLETED }  // status 기반으로 변경
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