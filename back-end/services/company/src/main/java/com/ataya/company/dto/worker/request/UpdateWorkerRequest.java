package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateWorkerRequest {
    private String name;
    private String surname;
    private String profilePicture;
}
