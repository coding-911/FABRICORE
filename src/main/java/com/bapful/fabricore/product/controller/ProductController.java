package com.bapful.fabricore.product.controller;

import com.bapful.fabricore.product.dto.request.ProductCreateRequest;
import com.bapful.fabricore.product.entity.Product;
import com.bapful.fabricore.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody ProductCreateRequest request) {
        UUID requesterId = UUID.randomUUID(); // 임시
        return productService.createProduct(request, requesterId);
    }

    @PostMapping("/{productId}/workflows/{workflowId}/approvals/{approvalId}/approve")
    public ResponseEntity<Void> approveWorkflow(
            @PathVariable Long productId,
            @PathVariable Long workflowId,
            @PathVariable Long approvalId,
            @RequestBody WorkflowApprovalActionRequest request
    ) {
        UUID approverId = UUID.randomUUID(); // 임시
        productService.approveWorkflow(productId, workflowId, approvalId, approverId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/workflows/{workflowId}/approvals/{approvalId}/reject")
    public ResponseEntity<Void> rejectWorkflow(
            @PathVariable Long productId,
            @PathVariable Long workflowId,
            @PathVariable Long approvalId,
            @RequestBody WorkflowApprovalActionRequest request
    ) {
        UUID approverId = UUID.randomUUID(); // 임시
        productService.rejectWorkflow(productId, workflowId, approvalId, approverId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/status/producing")
    public ResponseEntity<Void> changeToProducing(@PathVariable Long productId) {
        productService.changeToProducing(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/status/on-sale")
    public ResponseEntity<Void> changeToOnSale(@PathVariable Long productId) {
        productService.changeToOnSale(productId);
        return ResponseEntity.ok().build();
    }

}
