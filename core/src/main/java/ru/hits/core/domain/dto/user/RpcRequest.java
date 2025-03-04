package ru.hits.core.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public class RpcRequest {

    @JsonProperty("Id")
    private UUID Id;

    public RpcRequest() {}

    public RpcRequest(UUID id) {
        this.Id = id;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }
}