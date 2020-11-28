package com.zoltan.toth.molecule.service;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.model.repository.BasicCalculateResponse;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import com.zoltan.toth.molecule.repository.BasicMoleculeInfoRetriever;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {
    @Mock
    private BasicMoleculeInfoRetriever basicMoleculeInfoRetriever;

    @Mock
    private BasicMoleculeInfoMapper basicMoleculeInfoMapper;

    @InjectMocks
    private InfoService infoService;

    @Test
    public void shouldCreateRequestAndHandleResponse() {
        String molecule = "aspirin";

        BasicCalculateResponse[] basicCalculateResponses = new BasicCalculateResponse[1];
        basicCalculateResponses[0] = new BasicCalculateResponse("BASIC", "formula", "Formula", "C9H8O4");

        CalculateResponse calculateResponse = new CalculateResponse(Arrays.asList(basicCalculateResponses));

        when(basicMoleculeInfoRetriever.invokeCalculate(any())).thenReturn(calculateResponse);

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("Formula", "C9H8O4");

        BasicMoleculeInfo basicMoleculeInfo = new BasicMoleculeInfo(infoMap);

        when(basicMoleculeInfoMapper.map(calculateResponse)).thenReturn(basicMoleculeInfo);

        BasicMoleculeInfo actual = infoService.getBasicInfo(molecule);

        Assertions.assertEquals("C9H8O4", actual.getBasicMoleculeInfo().get("Formula"));
    }
}
