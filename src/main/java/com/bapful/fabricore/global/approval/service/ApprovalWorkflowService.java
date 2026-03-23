package com.bapful.fabricore.global.approval.service;

import com.bapful.fabricore.global.approval.enums.ApprovalDomainType;
import com.bapful.fabricore.product.entity.ProductStatus;
import com.bapful.fabricore.product.service.ProductStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApprovalWorkflowService {

    private final ApprovalService approvalService;
    private ProductStatusService productStatusService;



    public Object approve(ApprovalDomainType domainType, Long domainId) {
        log.info("[APPROVAL-WORKFLOW] 도메인 후처리 시작: domainType={}, domainId={}",
                domainType, domainId);

        switch (domainType) {
            case PRODUCT_STATUS:
                return productStatusService.applyApprovedStatusChange(domainId);
            case COST:
                throw new UnsupportedOperationException("COST 승인 후처리는 아직 구현되지 않았습니다.");
            case PRICING:
                throw new UnsupportedOperationException("PRICING 승인 후처리는 아직 구현되지 않았습니다.");
        }

        return null;
    }


}