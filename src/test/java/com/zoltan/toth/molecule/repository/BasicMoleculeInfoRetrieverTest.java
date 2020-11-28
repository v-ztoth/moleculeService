package com.zoltan.toth.molecule.repository;

import com.zoltan.toth.molecule.exceptions.CalculateApiException;
import com.zoltan.toth.molecule.model.repository.BasicCalculateResponse;
import com.zoltan.toth.molecule.model.repository.CalculateRequest;
import com.zoltan.toth.molecule.model.repository.CalculateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasicMoleculeInfoRetrieverTest {

    @Mock
    private RestTemplate restTemplate;

    private BasicMoleculeInfoRetriever basicMoleculeInfoRetriever;

    @BeforeEach
    public void init() {
        basicMoleculeInfoRetriever = new BasicMoleculeInfoRetriever(restTemplate, "endpoint", "apiKey");
    }

    @Test
    public void shouldRetrieveInfo() {
        CalculateRequest calculateRequest = new CalculateRequest("aspirin", Collections.singletonList("BASIC"));

        BasicCalculateResponse[] basicCalculateResponses = new BasicCalculateResponse[1];
        basicCalculateResponses[0] = new BasicCalculateResponse("BASIC", "formula", "Formula", "C9H8O4");

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(basicCalculateResponses);

        CalculateResponse calculateResponse = basicMoleculeInfoRetriever.invokeCalculate(calculateRequest);

        Assertions.assertNotNull(calculateResponse);
        Assertions.assertEquals(1, calculateResponse.getCalculateResponse().size());
        Assertions.assertEquals("BASIC", calculateResponse.getCalculateResponse().get(0).getGroup());
        Assertions.assertEquals("formula", calculateResponse.getCalculateResponse().get(0).getKey());
        Assertions.assertEquals("Formula", calculateResponse.getCalculateResponse().get(0).getLabel());
        Assertions.assertEquals("C9H8O4", calculateResponse.getCalculateResponse().get(0).getValue());
    }

    @Test
    public void shouldReturnEmptyListWhenNotFoundRetrieved() {
        CalculateRequest calculateRequest = new CalculateRequest("aspirin", Collections.singletonList("BASIC"));

        BasicCalculateResponse[] basicCalculateResponses = new BasicCalculateResponse[0];

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(basicCalculateResponses);

        CalculateResponse calculateResponse = basicMoleculeInfoRetriever.invokeCalculate(calculateRequest);

        Assertions.assertNotNull(calculateResponse);
        Assertions.assertEquals(0, calculateResponse.getCalculateResponse().size());
    }

    @Test
    public void shouldThrowCalculateApiExceptionWhenBadRequestRetrieved() {
        CalculateRequest calculateRequest = new CalculateRequest("aspirin", Collections.singletonList("BASIC"));

        when(restTemplate.postForObject(anyString(), any(), any()))
                .thenThrow(
                        HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "bad request", null, null, null)
                );

        Exception exception = Assertions.assertThrows(
                CalculateApiException.class,
                () -> basicMoleculeInfoRetriever.invokeCalculate(calculateRequest)
        );

        Assertions.assertEquals("Invalid molecule aspirin", exception.getMessage());
    }

    @Test
    public void shouldThrowCalculateApiExceptionWhenAnyClientErrorRetrieved() {
        CalculateRequest calculateRequest = new CalculateRequest("aspirin", Collections.singletonList("BASIC"));

        when(restTemplate.postForObject(anyString(), any(), any()))
                .thenThrow(
                        HttpClientErrorException.create(HttpStatus.FORBIDDEN, "forbidden", null, null, null)
                );

        Exception exception = Assertions.assertThrows(
                CalculateApiException.class,
                () -> basicMoleculeInfoRetriever.invokeCalculate(calculateRequest)
        );

        Assertions.assertTrue(exception.getMessage().contains("Calculate API Client error occurred"));
    }

    @Test
    public void shouldThrowCalculateApiExceptionWhenAnyApiServerErrorRetrieved() {
        CalculateRequest calculateRequest = new CalculateRequest("aspirin", Collections.singletonList("BASIC"));

        when(restTemplate.postForObject(anyString(), any(), any()))
                .thenThrow(
                        HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "internal", null, null, null)
                );

        Exception exception = Assertions.assertThrows(
                CalculateApiException.class,
                () -> basicMoleculeInfoRetriever.invokeCalculate(calculateRequest)
        );

        Assertions.assertTrue(exception.getMessage().contains("Calculate API Server exception occurred"));
    }

}
