package com.bapful.fabricore.product.service;

import com.bapful.fabricore.global.approval.dto.ApprovalCreateCommand;
import com.bapful.fabricore.global.approval.entity.ApprovalDocument;
import com.bapful.fabricore.global.approval.entity.ApprovalLine;
import com.bapful.fabricore.global.approval.enums.ApprovalDomainType;
import com.bapful.fabricore.global.approval.repository.ApprovalDocumentRepository;
import com.bapful.fabricore.global.approval.repository.ApprovalLineRepository;
import com.bapful.fabricore.global.approval.service.ApprovalService;
import com.bapful.fabricore.product.dto.request.StatusChangeRequest;
import com.bapful.fabricore.product.entity.Product;
import com.bapful.fabricore.product.entity.ProductStatus;
import com.bapful.fabricore.product.repository.ProductRepository;
import com.bapful.fabricore.product.repository.ProductStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductStatusService {

    private final ProductRepository productRepository;
    private final ProductStatusRepository productStatusRepository;
    private final ApprovalService approvalService;


    public void requestStatusChange(Long productId, StatusChangeRequest request, UUID requestedBy) {
        validateRequest(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        validateStatusTransition(product, request);

        ProductStatus productStatus = ProductStatus.builder()
                .product(product)
                .fromStatus(request.getFromStatus())
                .toStatus(request.getToStatus())
                .build();

        productStatusRepository.save(productStatus);

        Long approvalDocumentId = approvalService.createApproval(
                ApprovalCreateCommand.builder()
                        .domainType(ApprovalDomainType.PRODUCT_STATUS)
                        .domainId(productStatus.getId())
                        .requestedBy(requestedBy)
                        .comment(request.getComment())
                        .approverIds(request.getApproverIds())
                        .build()
        );

        productStatus.mapApprovalDocument(approvalDocumentId);
    }


    private void validateRequest(StatusChangeRequest request) {
        if (request.getFromStatus() == null) {
            throw new IllegalArgumentException("from_status는 필수입니다.");
        }
        if (request.getToStatus() == null) {
            throw new IllegalArgumentException("to_status는 필수입니다.");
        }
        if (request.getApproverIds() == null || request.getApproverIds().isEmpty()) {
            throw new IllegalArgumentException("결재선은 최소 1명 이상이어야 합니다.");
        }
    }

    private void validateStatusTransition(Product product, StatusChangeRequest request) {
        if (product.getLaunchStatus() != request.getFromStatus()) {
            throw new IllegalStateException("현재 출시 단계와 요청의 from_status가 일치하지 않습니다.");
        }
        if (request.getFromStatus() == request.getToStatus()) {
            throw new IllegalArgumentException("from_status와 to_status는 같을 수 없습니다.");
        }

        // 여기서 출시 프로세스 규칙 추가 가능
        // 예: DRAFT -> REVIEW -> APPROVED -> LAUNCHED
    }

    // 출시 단계 변경 결재 완료 시
    public ProductStatus applyApprovedStatusChange(Long productStatusId) {

        ProductStatus productStatus = productStatusRepository.findById(productStatusId)
                .orElseThrow(() -> new IllegalArgumentException("출시 단계 변경 요청이 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        productStatus.setStatusChangedAt(now);
        productStatusRepository.save(productStatus);

        Product product = productStatus.getProduct();
        product.changeLaunchStatus(productStatus.getToStatus());
        product.setUpdatedAt(now);
        productRepository.save(product);

        log.info("[PRODUCT-STATUS] 변경된 상품 출시 단계 변경 완료:  productId={}, from={}, to={}",
                product.getId(),
                productStatus.getFromStatus(),
                productStatus.getToStatus());

        return productStatus;
    }

    // 출시 단계 변경 결재 반려 시
    public void applyRejectedStatusChange(Long productStatusId) {
        ProductStatus productStatus = productStatusRepository.findById(productStatusId)
                .orElseThrow(() -> new IllegalArgumentException("출시 단계 변경 요청이 없습니다."));


        // 현재는 별도 상태필드 없으므로 추가 처리 없음
    }
}
