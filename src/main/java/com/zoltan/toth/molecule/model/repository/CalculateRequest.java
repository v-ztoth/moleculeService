package com.zoltan.toth.molecule.model.repository;

import lombok.Value;

import java.util.List;

@Value
public class CalculateRequest {
    String structure;
    List<String> calculations;
}
