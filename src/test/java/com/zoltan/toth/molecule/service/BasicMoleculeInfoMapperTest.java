package com.zoltan.toth.molecule.service;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.model.repository.BasicCalculateResponse;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BasicMoleculeInfoMapperTest {
    private BasicMoleculeInfoMapper basicMoleculeInfoMapper;

    @BeforeEach
    public void init() {
        basicMoleculeInfoMapper = new BasicMoleculeInfoMapper();
    }

    @Test
    public void shouldMap() {
        BasicCalculateResponse[] basicCalculateResponses = new BasicCalculateResponse[1];
        basicCalculateResponses[0] = new BasicCalculateResponse("BASIC", "formula", "Formula", "C9H8O4");

        CalculateResponse calculateResponse = new CalculateResponse(Arrays.asList(basicCalculateResponses));

        BasicMoleculeInfo basicMoleculeInfo = basicMoleculeInfoMapper.map(calculateResponse);

        Assertions.assertEquals("C9H8O4", basicMoleculeInfo.getBasicMoleculeInfo().get("Formula"));
    }
}
