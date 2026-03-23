package com.bapful.fabricore.global.approval.dto;

import com.bapful.fabricore.global.approval.enums.ApprovalDomainType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApprovalResult {

    private Long approvalDocumentId;
    private ApprovalDomainType domainType;
    private Long domainId;
    private boolean approvedCompleted;
    private boolean rejectedCompleted;
    private Object domainResult;

    public ApprovalResult withDomainResult(Object domainResult) {
        return ApprovalResult.builder()
                .approvalDocumentId(this.approvalDocumentId)
                .domainType(this.domainType)
                .domainId(this.domainId)
                .approvedCompleted(this.approvedCompleted)
                .rejectedCompleted(this.rejectedCompleted)
                .domainResult(domainResult)
                .build();
    }

}