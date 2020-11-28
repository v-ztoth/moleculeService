package com.zoltan.toth.molecule.model.repository;

import lombok.Data;

import java.util.List;

@Data
public class CalculateRequest {
    private String structure;
    private List<String> calculations;
}
