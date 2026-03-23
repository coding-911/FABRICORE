package com.bapful.fabricore.global.approval.dto;

import com.bapful.fabricore.global.approval.enums.ApprovalDomainType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ApprovalCreateCommand {

    private ApprovalDomainType domainType;
    private Long domainId;
    private UUID requestedBy;
    private String comment;
    private List<UUID> approverIds;

}
