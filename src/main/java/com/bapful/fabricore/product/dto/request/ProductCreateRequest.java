package com.bapful.fabricore.product.dto.request;

import com.bapful.fabricore.product.enums.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductCreateRequest {

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("season_code")
    private String seasonCode;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("color_code")
    private String colorCode;

    @JsonProperty("genderType")
    private GenderType genderType;

    @JsonProperty("description")
    private String description;

// TODO 사진 등록 구현 필요
//    List<ProductImageCreateRequest> images;

    private List<ReviewerCreateRequest> reviewers;

}
