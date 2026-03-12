package com.bapful.fabricore.product.enums;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
    PENDING("대기"),
    IN_PROGRESS("진행 중"),
    APPROVED("승인"),
    REJECTED("반려");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }
}

