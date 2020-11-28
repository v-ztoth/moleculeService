package com.zoltan.toth.molecule.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetBasicMoleculeInfoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private WireMockServer wireMockServer;

    @BeforeAll
    public void setup() {
        wireMockServer = new WireMockServer(
                options()
                        .extensions(new ResponseTemplateTransformer(false))
                        .port(8081));

        stubFor200();
        stubFor400();
        stubFor404();
        stubFor500();

        wireMockServer.start();
    }

    @Test
    public void shouldReturn200WhenApiResponseIs200() {
        String excepted = "{\"basicMoleculeInfo\":{\"Formula\":\"C9H8O4\",\"Composition\":\"C (60.00%), H (4.48%), O (35.52%)\",\"Molar mass [g/mol]\":\"180.159\",\"Lipinski's rule of five\":\"true\",\"Exact mass [Da]\":\"180.042258738\"}}";

        final ResponseEntity<String> response = restTemplate.exchange(
                "/info/basic?molecule=aspirin",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(excepted, response.getBody());
    }

    @Test
    public void shouldReturn400WhenApiResponseIs400() {

        final ResponseEntity<String> response = restTemplate.exchange(
                "/info/basic?molecule=aspirinBADREQUEST",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenApiResponseIs404() {

        final ResponseEntity<String> response = restTemplate.exchange(
                "/info/basic?molecule=aspirinNOTFOUND",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenApiResponseIs500() {

        final ResponseEntity<String> response = restTemplate.exchange(
                "/info/basic?molecule=aspirinSERVERERROR",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private void stubFor200() {
        String basicCalculateResponse =
                "[ {" +
                        "  \"group\" : \"BASIC\"," +
                        "  \"key\" : \"molar_mass\"," +
                        "  \"label\" : \"Molar mass [g/mol]\"," +
                        "  \"value\" : 180.159" +
                        "}, {" +
                        "  \"group\" : \"BASIC\"," +
                        "  \"key\" : \"exact_mass\"," +
                        "  \"label\" : \"Exact mass [Da]\"," +
                        "  \"value\" : 180.042258738" +
                        "}, {" +
                        "  \"group\" : \"BASIC\"," +
                        "  \"key\" : \"formula\"," +
                        "  \"label\" : \"Formula\"," +
                        "  \"value\" : \"C9H8O4\"" +
                        "}, {" +
                        "  \"group\" : \"BASIC\"," +
                        "  \"key\" : \"composition\"," +
                        "  \"label\" : \"Composition\"," +
                        "  \"value\" : \"C (60.00%), H (4.48%), O (35.52%)\"" +
                        "}, {" +
                        "  \"group\" : \"BASIC\"," +
                        "  \"key\" : \"lipinski\"," +
                        "  \"label\" : \"Lipinski's rule of five\"," +
                        "  \"value\" : true" +
                        "} ]";

        wireMockServer.stubFor(post(urlEqualTo("https://api.chemicalize.com/v1/calculate"))
                .withRequestBody(equalToJson("{" +
                        "  \"structure\": \"aspirin\"," +
                        "  \"calculations\": [" +
                        "    \"BASIC\"" +
                        "  ]" +
                        "}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(basicCalculateResponse)));
    }

    private void stubFor404() {
        wireMockServer.stubFor(post(urlEqualTo("https://api.chemicalize.com/v1/calculate"))
                .withRequestBody(equalToJson("{" +
                        "  \"structure\": \"aspirinNOTFOUND\"," +
                        "  \"calculations\": [" +
                        "    \"BASIC\"" +
                        "  ]" +
                        "}"))
                .willReturn(aResponse()));
    }

    private void stubFor400() {
        wireMockServer.stubFor(post(urlEqualTo("https://api.chemicalize.com/v1/calculate"))
                .withRequestBody(equalToJson("{" +
                        "  \"structure\": \"aspirinBADREQUEST\"," +
                        "  \"calculations\": [" +
                        "    \"BASIC\"" +
                        "  ]" +
                        "}"))
                .willReturn(aResponse()
                        .withStatus(400)));
    }

    private void stubFor500() {
        wireMockServer.stubFor(post(urlEqualTo("https://api.chemicalize.com/v1/calculate"))
                .withRequestBody(equalToJson("{" +
                        "  \"structure\": \"aspirinSERVERERROR\"," +
                        "  \"calculations\": [" +
                        "    \"BASIC\"" +
                        "  ]" +
                        "}"))
                .willReturn(aResponse()
                        .withStatus(500)));
    }

}
