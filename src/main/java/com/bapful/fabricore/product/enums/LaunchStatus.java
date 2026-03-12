package com.bapful.fabricore.product.enums;

import lombok.Getter;

@Getter
public enum LaunchStatus {
    PLANNING("기획"),
    REVIEWING("출시 검토"),
    PRODUCTION_READY("생산 확정"),
    PRODUCING("생산 중"),
    ON_SALE("판매 중"),
    SOLD_OUT("품절"),
    END_OF_SALE("판매 종료");

    private final String label;

    LaunchStatus(String label) {
        this.label = label;
    }

    public static LaunchStatus from(String value) {
        return LaunchStatus.valueOf(value);
    }

}
