package com.bapful.fabricore.global.approval.enums;

public enum ApprovalDomainType {

    PRODUCT_STATUS("상품 출시 단계"),

    //TODO 추후 실제 결재 필요한 내용 추가 및 공통 코드로 빠질 가능성 있음
    COST("원가"),
    PRICING("가격");


    private final String label;

    ApprovalDomainType(String label) { this.label = label; }

}
