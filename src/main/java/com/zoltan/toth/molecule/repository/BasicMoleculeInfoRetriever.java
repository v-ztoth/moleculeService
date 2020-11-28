package com.zoltan.toth.molecule.repository;

import com.zoltan.toth.molecule.exceptions.CalculateApiException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BasicMoleculeInfoRetriever {

    private final RestTemplate restTemplate;

    private final String calculateEndpoint;

    private final String apiKey;

    @Autowired
    public BasicMoleculeInfoRetriever(RestTemplate restTemplate,
                                      @Value("${calculate-endpoint}") String calculateEndpoint,
                                      @Value("${X-Api-Key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.calculateEndpoint = calculateEndpoint;
        this.apiKey = apiKey;
    }

    public CalculateResponse invokeCalculate(final CalculateRequest calculateRequest) {
        log.debug("CalculateRequest: " + calculateRequest);
        log.debug("calculateEndpoint: " + calculateEndpoint);
        log.debug("apiKey: " + apiKey);

        HttpEntity<CalculateRequest> request = getHttpEntity(calculateRequest);

        BasicCalculateResponse[] basicCalculateResponses = getBasicCalculateResponses(request, calculateRequest.getStructure());

        if (basicCalculateResponses != null) {
            List<BasicCalculateResponse> basicCalculateResponseList = toList(basicCalculateResponses);

            return new CalculateResponse(basicCalculateResponseList);
        }

        return new CalculateResponse(Collections.emptyList());
    }

    private HttpEntity<CalculateRequest> getHttpEntity(CalculateRequest calculateRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", apiKey);

        HttpEntity<CalculateRequest> request = new HttpEntity<>(calculateRequest, headers);

        log.debug("request: " + request);
        return request;
    }

    private BasicCalculateResponse[] getBasicCalculateResponses(HttpEntity<CalculateRequest> request, String molecule) {
        BasicCalculateResponse[] basicCalculateResponses = null;

        try {
            basicCalculateResponses = restTemplate.postForObject(
                    calculateEndpoint, request, BasicCalculateResponse[].class
            );
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case NOT_FOUND:
                    log.info("No data found for molecule " + molecule);
                    break;
                case BAD_REQUEST:
                    log.info("Invalid molecule " + molecule);
                    throw new CalculateApiException("Invalid molecule " + molecule);
                default:
                    log.info("Calculate API Client error occurred " + ex);
                    throw new CalculateApiException("Calculate API Client error occurred " + ex.getMessage());
            }
        } catch (HttpServerErrorException ex) {
            log.info("Calculate API Client exception occurred " + ex);
            throw new CalculateApiException("Calculate API Server exception occurred " + ex.getMessage());
        }
        return basicCalculateResponses;
    }

    private List<BasicCalculateResponse> toList(BasicCalculateResponse[] basicCalculateResponses) {
        Arrays.stream(basicCalculateResponses)
                .forEach(res -> log.debug("basicCalculateResponses: " + res));

        List<BasicCalculateResponse> basicCalculateResponseList = Arrays.stream(basicCalculateResponses)
                .collect(Collectors.toList());
        return basicCalculateResponseList;
    }

}
