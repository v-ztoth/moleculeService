package com.zoltan.toth.molecule.service;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.model.repository.CalculateRequest;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import com.zoltan.toth.molecule.repository.BasicMoleculeInfoRetriever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InfoService {

    private final BasicMoleculeInfoRetriever basicMoleculeInfoRetriever;

    private final BasicMoleculeInfoMapper basicMoleculeInfoMapper;

    @Autowired
    public InfoService(BasicMoleculeInfoRetriever basicMoleculeInfoRetriever,
                       BasicMoleculeInfoMapper basicMoleculeInfoMapper) {
        this.basicMoleculeInfoRetriever = basicMoleculeInfoRetriever;
        this.basicMoleculeInfoMapper = basicMoleculeInfoMapper;
    }

    public BasicMoleculeInfo getBasicInfo(String molecule) {
        log.debug("molecule: " + molecule);

        List<String> calculations = new ArrayList<>();
        calculations.add(CalculationGroups.BASIC.name());

        CalculateRequest calculateRequest = new CalculateRequest(molecule, calculations);

        CalculateResponse calculateResponse = basicMoleculeInfoRetriever.invokeCalculate(calculateRequest);

        return basicMoleculeInfoMapper.map(calculateResponse);
    }

}
