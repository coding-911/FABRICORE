package com.bapful.fabricore.product.entity;

import com.bapful.fabricore.product.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_status_approval")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStatusApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_status_approval_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_status_id", nullable = false)
    private ProductStatus productStatus;

    @Column(name = "approved_by", nullable = false)
    private UUID approvedBy;

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
    public ProductStatusApproval(
            ProductStatus productStatus,
            UUID approvedBy,
            Integer approvalOrder
    ) {
        this.productStatus = productStatus;
        this.approvedBy = approvedBy;
        this.approvalOrder = approvalOrder;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public void approve(String comment, LocalDateTime actedAt) {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.comment = comment;
        this.actedAt = actedAt;
    }

    public void reject(String comment, LocalDateTime actedAt) {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.comment = comment;
        this.actedAt = actedAt;
    }

    public boolean isApproved() {
        return this.approvalStatus == ApprovalStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.approvalStatus == ApprovalStatus.REJECTED;
    }
}
