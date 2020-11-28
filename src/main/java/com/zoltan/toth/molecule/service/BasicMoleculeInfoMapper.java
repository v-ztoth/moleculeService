package com.zoltan.toth.molecule.service;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BasicMoleculeInfoMapper {

    public BasicMoleculeInfo map(final CalculateResponse calculateResponse) {
        log.debug("CalculateResponse: " + calculateResponse);

        Map<String, String> info = new HashMap<>();

        calculateResponse
                .getCalculateResponse()
                .forEach(
                        res -> info.put(res.getLabel(), res.getValue())
                );

        info.forEach((key, value) -> log.debug("infoEntry: " + key + " " + value));

        return new BasicMoleculeInfo(info);
    }

}
