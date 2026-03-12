package com.bapful.fabricore.product.entity;

import com.bapful.fabricore.product.enums.ApprovalStatus;
import com.bapful.fabricore.product.enums.LaunchStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_status_workflow")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_status_workflow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private LaunchStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private LaunchStatus toStatus;

    @Column(name = "requested_by", nullable = false)
    private UUID requestedBy;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    private ApprovalStatus approvalStatus;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public ProductStatus(
            Product product,
            LaunchStatus fromStatus,
            LaunchStatus toStatus,
            UUID requestedBy,
            LocalDateTime requestedAt,
            String comment,
            LocalDateTime completedAt
    ) {
        this.product = product;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.comment = comment;
        this.completedAt = completedAt;
    }

    public void startApproval() {
        this.approvalStatus = ApprovalStatus.IN_PROGRESS;
    }

    public void approveAll() {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.completedAt = LocalDateTime.now();
    }

    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.completedAt = LocalDateTime.now();
    }
}
