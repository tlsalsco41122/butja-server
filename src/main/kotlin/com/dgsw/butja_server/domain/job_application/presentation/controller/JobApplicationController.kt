package com.dgsw.butja_server.domain.job_application.presentation.controller

import com.dgsw.butja_server.domain.job_application.presentation.dto.req.CreateApplicationReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.req.UpdateApplicationReq
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.JobApplicationDetailRes
import com.dgsw.butja_server.domain.job_application.presentation.dto.res.JobApplicationRes
import com.dgsw.butja_server.domain.job_application.service.JobApplicationService
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
@RequestMapping("/api/applications")
class JobApplicationController(
    private val jobApplicationService: JobApplicationService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody req: CreateApplicationReq): ApiResponse<JobApplicationDetailRes> {
        return ApiResponse.success(jobApplicationService.create(req))
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getList(): ApiResponse<List<JobApplicationRes>> {
        return ApiResponse.success(jobApplicationService.getList())
    }

    @GetMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDetail(@PathVariable applicationId: Long): ApiResponse<JobApplicationDetailRes> {
        return ApiResponse.success(jobApplicationService.getDetail(applicationId))
    }

    @PatchMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable applicationId: Long,
        @Valid @RequestBody req: UpdateApplicationReq
    ): ApiResponse<JobApplicationDetailRes> {
        return ApiResponse.success(jobApplicationService.update(applicationId, req))
    }

    @DeleteMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@PathVariable applicationId: Long): ApiResponse<Nothing> {
        jobApplicationService.delete(applicationId)
        return ApiResponse.success(null)
    }
}
