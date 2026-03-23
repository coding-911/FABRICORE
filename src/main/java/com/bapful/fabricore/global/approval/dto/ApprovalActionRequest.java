package com.bapful.fabricore.global.approval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApprovalActionRequest {
    @JsonProperty("comment")
    private String comment;
}
