package com.zoltan.toth.molecule.model.repository;

import lombok.Data;

import java.util.List;

@Data
public class CalculateResponse {
    private List<BasicCalculateResponse> calculateResponse;
}
