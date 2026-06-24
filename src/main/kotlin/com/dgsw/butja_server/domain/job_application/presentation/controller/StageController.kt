package com.dgsw.butja_server.domain.job_application.presentation.controller

import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CompleteStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CreateStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateStageOrderReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateStageReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.CurrentStageRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.ProgressRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.StageRes
import com.dgsw.butja_server.domain.job_application.service.StageService
import com.dgsw.butja_server.global.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applications/{applicationId}/stages")
class StageController(
    private val stageService: StageService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable applicationId: Long,
        @Valid @RequestBody req: CreateStageReq
    ): ApiResponse<StageRes> {
        return ApiResponse.success(stageService.create(applicationId, req))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getList(@PathVariable applicationId: Long): ApiResponse<List<StageRes>> {
        return ApiResponse.success(stageService.getList(applicationId))
    }

    @GetMapping("/{stageId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDetail(
        @PathVariable applicationId: Long,
        @PathVariable stageId: Long
    ): ApiResponse<StageRes> {
        return ApiResponse.success(stageService.getDetail(applicationId, stageId))
    }

    @PatchMapping("/{stageId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable applicationId: Long,
        @PathVariable stageId: Long,
        @Valid @RequestBody req: UpdateStageReq
    ): ApiResponse<StageRes> {
        return ApiResponse.success(stageService.update(applicationId, stageId, req))
    }

    @DeleteMapping("/{stageId}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(
        @PathVariable applicationId: Long,
        @PathVariable stageId: Long
    ): ApiResponse<Nothing> {
        stageService.delete(applicationId, stageId)
        return ApiResponse.success(null)
    }

    @PatchMapping("/{stageId}/order")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrder(
        @PathVariable applicationId: Long,
        @PathVariable stageId: Long,
        @Valid @RequestBody req: UpdateStageOrderReq
    ): ApiResponse<List<StageRes>> {
        return ApiResponse.success(stageService.updateOrder(applicationId, stageId, req))
    }

    @PatchMapping("/{stageId}/complete")
    @ResponseStatus(HttpStatus.OK)
    fun complete(
        @PathVariable applicationId: Long,
        @PathVariable stageId: Long,
        @RequestBody req: CompleteStageReq
    ): ApiResponse<StageRes> {
        return ApiResponse.success(stageService.complete(applicationId, stageId, req))
    }

    @GetMapping("/progress")
    @ResponseStatus(HttpStatus.OK)
    fun getProgress(@PathVariable applicationId: Long): ApiResponse<ProgressRes> {
        return ApiResponse.success(stageService.getProgress(applicationId))
    }

    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    fun getCurrentStage(@PathVariable applicationId: Long): ApiResponse<CurrentStageRes> {
        return ApiResponse.success(stageService.getCurrentStage(applicationId))
    }
}
