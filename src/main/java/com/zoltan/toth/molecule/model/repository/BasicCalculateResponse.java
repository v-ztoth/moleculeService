package com.zoltan.toth.molecule.model.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(builderClassName = "ResponseBuilder")
@JsonDeserialize(builder = BasicCalculateResponse.ResponseBuilder.class)
public class BasicCalculateResponse {
    String group;
    String key;
    String label;
    String value;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ResponseBuilder {
    }
}
