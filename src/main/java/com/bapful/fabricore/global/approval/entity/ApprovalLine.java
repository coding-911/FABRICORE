package com.bapful.fabricore.global.approval.entity;

import com.bapful.fabricore.global.approval.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "approval_line",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_approval_line_document_order",
                        columnNames = {"approval_document_id", "approval_order"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalLine {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_line_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approval_document_id", nullable = false)
    private ApprovalDocument approvalDocument;

    @Column(name = "approver_id", nullable = false)
    private UUID approverId;

    @Column(name = "approval_order", nullable = false)
    private Integer approvalOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    private ApprovalStatus approvalStatus;

    @Column(name = "acted_at")
    private LocalDateTime actedAt;

    @Column(name = "comment", length = 500)
    private String comment;

    @Builder
    public ApprovalLine(
            ApprovalDocument approvalDocument,
            UUID approverId,
            Integer approvalOrder
    ) {
        this.approvalDocument = approvalDocument;
        this.approverId = approverId;
        this.approvalOrder = approvalOrder;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void approve(String comment) {
        if (this.approvalStatus != ApprovalStatus.PENDING) {
            throw new IllegalStateException("대기 상태의 결재선만 승인할 수 있습니다.");
        }
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.comment = comment;
        this.actedAt = LocalDateTime.now();
    }

    public void reject(String comment) {
        if (this.approvalStatus != ApprovalStatus.PENDING) {
            throw new IllegalStateException("대기 상태의 결재선만 반려할 수 있습니다.");
        }
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.comment = comment;
        this.actedAt = LocalDateTime.now();
    }

    public boolean isApproved() {
        return this.approvalStatus == ApprovalStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.approvalStatus == ApprovalStatus.REJECTED;
    }

    public boolean isPending() { return this.approvalStatus == ApprovalStatus.PENDING; }

}
