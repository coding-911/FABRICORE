package com.bapful.fabricore.global.approval.entity;

import com.bapful.fabricore.global.approval.enums.ApprovalDomainType;
import com.bapful.fabricore.global.approval.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "approval_document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_document_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type", nullable = false, length = 50)
    private ApprovalDomainType domainType;

    @Column(name = "domain_id", nullable = false)
    private Long domainId;

    @Column(name = "requested_by", nullable = false)
    private java.util.UUID requestedBy;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    private ApprovalStatus approvalStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "comment", length = 500)
    private String comment;

    @Builder
    public ApprovalDocument(
            ApprovalDomainType domainType,
            Long domainId,
            java.util.UUID requestedBy,
            LocalDateTime requestedAt,
            String comment
    ) {
        this.domainType = domainType;
        this.domainId = domainId;
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt;
        this.comment = comment;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void startApproval() {
        this.approvalStatus = ApprovalStatus.IN_PROGRESS;
    }

    public void approveAll() {
        if (this.approvalStatus == ApprovalStatus.REJECTED) {
            throw new IllegalStateException("반려된 결재는 승인 완료 처리할 수 없습니다.");
        }
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.completedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.approvalStatus == ApprovalStatus.APPROVED) {
            throw new IllegalStateException("이미 승인 완료된 결재는 반려할 수 없습니다.");
        }
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isFinished() {
        return this.approvalStatus == ApprovalStatus.APPROVED
                || this.approvalStatus == ApprovalStatus.REJECTED;
    }

}
