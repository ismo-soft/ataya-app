package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeUsernameRequest {
    @NotNull(message = "id is required")
    @NotEmpty(message = "can not be empty")
    private String id;

    @NotNull(message = "newUsername is required")
    @NotEmpty(message = "can not be empty")

    @JsonProperty("new-username")
    private String newUsername;

    @NotNull(message = "password is required")
    @NotEmpty(message = "can not be empty")
    private String password;
}
