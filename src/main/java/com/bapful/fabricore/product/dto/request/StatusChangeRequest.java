package com.bapful.fabricore.product.dto.request;

import com.bapful.fabricore.product.enums.LaunchStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class StatusChangeRequest {

    @JsonProperty("from_status")
    private LaunchStatus fromStatus;

    @JsonProperty("to_status")
    private LaunchStatus toStatus;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("approver_ids")
    private List<UUID> approverIds;
}
