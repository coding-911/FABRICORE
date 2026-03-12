package com.bapful.fabricore.product.enums;

import lombok.Getter;

@Getter
public enum GenderType {
    MALE("남성"),
    FEMALE("여성"),
    UNISEX("공용");

    private final String label;

    GenderType(String label) {
        this.label = label;
    }

    public static GenderType from(String value) {
        return GenderType.valueOf(value);
    }

}
