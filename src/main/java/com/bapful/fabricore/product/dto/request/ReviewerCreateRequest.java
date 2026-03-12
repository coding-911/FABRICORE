package com.bapful.fabricore.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
public class ReviewerCreateRequest {

    @NonNull
    @JsonProperty("approval_order")
    private Integer approvalOrder;

    @NonNull
    @JsonProperty("approved_by")
    private UUID approvedBy;

}
