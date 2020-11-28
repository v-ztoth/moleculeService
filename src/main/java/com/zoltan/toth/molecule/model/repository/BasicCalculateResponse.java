package com.zoltan.toth.molecule.model.repository;

import lombok.Data;

@Data
public class BasicCalculateResponse {
    private String group;
    private String key;
    private String label;
    private String value;
}
