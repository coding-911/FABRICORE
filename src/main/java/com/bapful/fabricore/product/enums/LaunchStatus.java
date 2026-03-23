package com.bapful.fabricore.product.enums;

import lombok.Getter;

@Getter
public enum LaunchStatus {
    PLANNING("기획", 1),
    REVIEWING("출시 검토", 2),
    PRODUCTION_READY("생산 확정", 3),
    PRODUCING("생산 중", 4),
    ON_SALE("판매 중", 5),
    SOLD_OUT("품절", 6),
    END_OF_SALE("판매 종료", 7);

    private final String label;
    private final int no;

    LaunchStatus(String label, int no) {
        this.label = label;
        this.no = no;
    }

    public static LaunchStatus from(String value) {
        return LaunchStatus.valueOf(value);
    }

}
