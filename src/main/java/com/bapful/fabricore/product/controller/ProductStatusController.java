package com.bapful.fabricore.product.controller;

import com.bapful.fabricore.product.dto.request.StatusChangeRequest;
import com.bapful.fabricore.product.service.ProductStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/{product_id}/status")
public class ProductStatusController {

    private final ProductStatusService productStatusService;

    // 상품 출시 단계 변경 결재 요청
    @PostMapping("request-status-change")
    public void requestStatusChange(
            @PathVariable("product_id") Long productId,
            @RequestBody StatusChangeRequest request
            ) {
        UUID requestedBy = getCurrentUserId();
        productStatusService.requestStatusChange(productId, request, requestedBy);
    }

    // 상품 출시 단계 개별 결재
    @PostMapping("/{status_id}/approvals/{approval_id}")
    public Boolean approveStatus(
            @PathVariable("product_id") Long productId,
            @PathVariable("status_id") Long statusId,
            @PathVariable("approval_id") Long approvalId,
            @RequestParam("approve_yn") Character approveYn
    ) {
        UUID approverId = UUID.randomUUID(); // 임시
        productStatusService.approveStatus(productId, statusId, approvalId, approverId, approveYn);
        return false;
    }

    private UUID getCurrentUserId() {
        // TODO SecurityContext에서 추출
        return UUID.fromString("11111111-1111-1111-1111-111111111111");
    }



}
