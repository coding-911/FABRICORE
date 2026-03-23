package com.bapful.fabricore.global.approval.controller;

import com.bapful.fabricore.global.approval.dto.ApprovalActionRequest;
import com.bapful.fabricore.global.approval.dto.ApprovalResult;
import com.bapful.fabricore.global.approval.service.ApprovalService;
import com.bapful.fabricore.global.approval.service.ApprovalWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;
    private final ApprovalWorkflowService approvalWorkflowService;



    // 결재 승인
    @PostMapping("/{approvalDocumentId}/approve")
    public ApprovalResult approve(
            @PathVariable Long approvalDocumentId,
            @RequestBody ApprovalActionRequest request
    ) {

        log.info("승인 요청 수신: approvalDocumentId={}, request={}",
                approvalDocumentId, request);

        UUID approverId = getCurrentUserId();

        /*
        해당 결재 라인 승인 처리
         */
        ApprovalResult result = approvalService.approve(approvalDocumentId, approverId, request.getComment());

        /*
        모든 결재 승인 완료 시
        해당 결재의 목적 도메인 후처리
         */
        if(!result.isApprovedCompleted()) {
            return result;
        }

        Object domainResult = approvalWorkflowService.approve(
                result.getDomainType(),
                result.getDomainId()
        );

        return result.withDomainResult(domainResult);
    }

    // 결재 반려
    @PostMapping("/{approvalDocumentId}/reject")
    public ApprovalResult reject(
            @PathVariable Long approvalDocumentId,
            @RequestBody ApprovalActionRequest request
    ) {
        UUID approverId = getCurrentUserId();
        ApprovalResult result = approvalService.reject(approvalDocumentId, approverId, request.getComment());
        return result;
    }

    private UUID getCurrentUserId() {
        return UUID.fromString("22222222-2222-2222-2222-222222222222");
    }
}