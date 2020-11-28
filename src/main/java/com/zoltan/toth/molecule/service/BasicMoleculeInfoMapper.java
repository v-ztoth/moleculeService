package com.zoltan.toth.molecule.service;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BasicMoleculeInfoMapper {

    public BasicMoleculeInfo map(final CalculateResponse calculateResponse) {
        Map<String, String> info = new HashMap<>();

        calculateResponse
                .getCalculateResponse()
                .forEach(
                        res -> info.put(res.getLabel(), res.getValue())
                );

        return new BasicMoleculeInfo(info);
    }

}
