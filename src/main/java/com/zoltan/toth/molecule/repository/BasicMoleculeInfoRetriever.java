package com.zoltan.toth.molecule.repository;

import com.zoltan.toth.molecule.model.repository.BasicCalculateResponse;
import com.zoltan.toth.molecule.model.repository.CalculateRequest;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BasicMoleculeInfoRetriever {

    private final RestTemplate restTemplate;

    @Value("${calculate-endpoint}")
    private String calculateEndpoint;

    @Value("${X-Api-Key}")
    private String apiKey;

    @Autowired
    public BasicMoleculeInfoRetriever(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CalculateResponse invokeCalculate(final CalculateRequest calculateRequest) {
        log.debug("CalculateRequest: " + calculateRequest);
        log.debug("calculateEndpoint: " + calculateEndpoint);
        log.debug("apiKey: " + apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", apiKey);

        HttpEntity<CalculateRequest> request = new HttpEntity<>(calculateRequest, headers);

        log.debug("request: " + request);

        BasicCalculateResponse[] basicCalculateResponses = restTemplate.postForObject(
                calculateEndpoint, request, BasicCalculateResponse[].class
        );

        if (basicCalculateResponses != null) {
            Arrays.stream(basicCalculateResponses)
                    .forEach(res -> log.debug("basicCalculateResponses: " + res));

            List<BasicCalculateResponse> basicCalculateResponseList = Arrays.stream(basicCalculateResponses)
                    .collect(Collectors.toList());

            return new CalculateResponse(basicCalculateResponseList);
        }

        return new CalculateResponse(Collections.emptyList());
    }

}
