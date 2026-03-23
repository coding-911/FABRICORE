package com.bapful.fabricore.product.entity;

import com.bapful.fabricore.global.approval.entity.ApprovalDocument;
import com.bapful.fabricore.global.approval.enums.ApprovalStatus;
import com.bapful.fabricore.product.enums.LaunchStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_status_id")
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

    @Column(name = "approval_document_id", unique = true)
    private Long approvalDocumentId;

    @Column(name = "stauts_changed_at", nullable = false)
    private LocalDateTime statusChangedAt;

    @Builder
    public ProductStatus(
            Product product,
            LaunchStatus fromStatus,
            LaunchStatus toStatus,
            LocalDateTime statusChangedAt
    ) {
        this.product = product;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.statusChangedAt = statusChangedAt;
    }

    public void mapApprovalDocument(Long approvalDocumentId) {
        if (this.approvalDocumentId != null) {
            throw new IllegalStateException("이미 결재 문서가 연결되어 있습니다.");
        }
        this.approvalDocumentId = approvalDocumentId;
    }

    public void setStatusChangedAt(LocalDateTime statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

}
