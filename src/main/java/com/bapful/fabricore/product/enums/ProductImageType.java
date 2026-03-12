package com.bapful.fabricore.product.enums;

import lombok.Getter;

@Getter
public enum ProductImageType {
    MAIN("대표 이미지"),
    DETAIL("상세 이미지"),
    LOOKBOOK("룩북 이미지"),
    SNAP("스냅 이미지");

    private final String description;

    ProductImageType(String description) {
        this.description = description;
    }
}
