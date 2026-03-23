package com.bapful.fabricore.global.approval.service;

import com.bapful.fabricore.global.approval.dto.ApprovalCreateCommand;
import com.bapful.fabricore.global.approval.dto.ApprovalResult;
import com.bapful.fabricore.global.approval.entity.ApprovalDocument;
import com.bapful.fabricore.global.approval.entity.ApprovalLine;
import com.bapful.fabricore.global.approval.repository.ApprovalDocumentRepository;
import com.bapful.fabricore.global.approval.repository.ApprovalLineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApprovalService {

    private final ApprovalDocumentRepository approvalDocumentRepository;
    private final ApprovalLineRepository approvalLineRepository;

    public Long createApproval(ApprovalCreateCommand command) {
        validateCreateCommand(command);

        ApprovalDocument approvalDocument = ApprovalDocument.builder()
                .domainType(command.getDomainType())
                .domainId(command.getDomainId())
                .requestedBy(command.getRequestedBy())
                .requestedAt(LocalDateTime.now())
                .comment(command.getComment())
                .build();

        approvalDocument.startApproval();
        approvalDocumentRepository.save(approvalDocument);

        List<ApprovalLine> approvalLines = new ArrayList<>();
        for (int i = 0; i < command.getApproverIds().size(); i++) {
            approvalLines.add(
                    ApprovalLine.builder()
                            .approvalDocument(approvalDocument)
                            .approverId(command.getApproverIds().get(i))
                            .approvalOrder(i + 1)
                            .build()
            );
        }
        approvalLineRepository.saveAll(approvalLines);

        return approvalDocument.getId();
    }

    public ApprovalResult approve(Long approvalDocumentId, UUID approverId, String comment) {

        log.info("[APPROVAL] 결재 승인 시도: approvalDocumentId={}, approverId={}",
                approvalDocumentId, approverId);

        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(approvalDocumentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결재 문서입니다."));

        if (approvalDocument.isFinished()) {
            throw new IllegalStateException("이미 종료된 결재입니다.");
        }

        List<ApprovalLine> approvalLines =
                approvalLineRepository.findByApprovalDocumentIdOrderByApprovalOrderAsc(approvalDocumentId);

        ApprovalLine currentLine = getCurrentPendingLine(approvalLines);
        if (currentLine == null) {
            throw new IllegalStateException("처리 가능한 결재선이 없습니다.");
        }

        if (!currentLine.getApproverId().equals(approverId)) {
            throw new IllegalStateException("현재 결재 차례가 아닙니다.");
        }

        currentLine.approve(comment);
        log.info("[APPROVAL] 결재선 승인 완료: approvalDocumentId={}, approverId={}, order={}",
                approvalDocumentId, approverId, currentLine.getApprovalOrder());

        boolean allApproved = approvalLines.stream().allMatch(ApprovalLine::isApproved);
        if (allApproved) {
            approvalDocument.approveAll();
            log.info("[APPROVAL] 결재 최종 승인 완료: approvalDocumentId={}, domainType={}, domainId={}",
                    approvalDocument.getId(),
                    approvalDocument.getDomainType(),
                    approvalDocument.getDomainId());
        }

        return ApprovalResult.builder()
                .approvalDocumentId(approvalDocument.getId())
                .domainType(approvalDocument.getDomainType())
                .domainId(approvalDocument.getDomainId())
                .approvedCompleted(allApproved)
                .rejectedCompleted(false)
                .build();
    }

    public ApprovalResult reject(Long approvalDocumentId, UUID approverId, String comment) {
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(approvalDocumentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결재 문서입니다."));

        if (approvalDocument.isFinished()) {
            throw new IllegalStateException("이미 종료된 결재입니다.");
        }

        List<ApprovalLine> approvalLines =
                approvalLineRepository.findByApprovalDocumentIdOrderByApprovalOrderAsc(approvalDocumentId);

        ApprovalLine currentLine = getCurrentPendingLine(approvalLines);
        if (currentLine == null) {
            throw new IllegalStateException("처리 가능한 결재선이 없습니다.");
        }

        if (!currentLine.getApproverId().equals(approverId)) {
            throw new IllegalStateException("현재 결재 차례가 아닙니다.");
        }

        currentLine.reject(comment);
        approvalDocument.reject();
        log.info("[APPROVAL] 결재 반려: approvalDocumentId={}, approverId={}, reason={}",
                approvalDocumentId, approverId, comment);

        return ApprovalResult.builder()
                .approvalDocumentId(approvalDocument.getId())
                .domainType(approvalDocument.getDomainType())
                .domainId(approvalDocument.getDomainId())
                .approvedCompleted(false)
                .rejectedCompleted(true)
                .build();
    }

    private ApprovalLine getCurrentPendingLine(List<ApprovalLine> approvalLines) {
        return approvalLines.stream()
                .filter(ApprovalLine::isPending)
                .findFirst()
                .orElse(null);
    }

    private void validateCreateCommand(ApprovalCreateCommand command) {
        if (command.getDomainType() == null) {
            throw new IllegalArgumentException("domainType은 필수입니다.");
        }
        if (command.getDomainId() == null) {
            throw new IllegalArgumentException("domainId는 필수입니다.");
        }
        if (command.getRequestedBy() == null) {
            throw new IllegalArgumentException("requestedBy는 필수입니다.");
        }
        if (command.getApproverIds() == null || command.getApproverIds().isEmpty()) {
            throw new IllegalArgumentException("결재선은 최소 1명 이상이어야 합니다.");
        }
    }

}
