package com.zoltan.toth.molecule.model.repository;

import lombok.Value;

import java.util.List;

@Value
public class CalculateResponse {
    List<BasicCalculateResponse> calculateResponse;
}
