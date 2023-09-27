package org.lookout_studios.meals_management_system.meals_management_system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import net.minidev.json.JSONObject;

@WireMockTest
class RegistrationServiceTests {

    static WireMockServer wireMockServer = new WireMockServer();
    private static TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeAll
    public static void beforeAll() {
        WireMock.configureFor(8080);
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    private String requestUrl = "http://localhost:8080/register";

    /**
     * Ensures that /register requests with invalid emails generate
     * Bad Request Response
     * 
     * @throws Exception
     */
    @Test
    public void registerInvalidEmailUserFail() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "abc-@mail.com");
        requestBody.put("password", "12345678");
        String responseBody = new ResponseBody(HttpStatus.BAD_REQUEST).getResponseBody();
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/register"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.BAD_REQUEST.value())
                                .withBody(responseBody)));
        ResponseEntity<String> response = restTemplate.postForEntity(
                requestUrl,
                requestBody,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    /**
     * Ensures that /register requests with invalid passwords generate
     * Bad Request response
     *
     * @throws Exception
     */
    @Test
    public void registerInvalidPasswordUserFail() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "abc@mail.com");
        requestBody.put("password", null);
        String responseBody = new ResponseBody(HttpStatus.BAD_REQUEST).getResponseBody();
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/register"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.BAD_REQUEST.value())
                                .withBody(responseBody)));
        ResponseEntity<String> response = restTemplate.postForEntity(
                requestUrl,
                requestBody,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());

    }

    /**
     * Ensures that valid /register requests generate OK response
     *
     * @throws Exception
     */
    @Test
    public void registerExistingUserFail() throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "abc@mail.com");
        requestBody.put("password", "12345678");
        String responseBody = new ResponseBody(HttpStatus.OK).getResponseBody();
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/register"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withBody(responseBody)));
        ResponseEntity<String> response = restTemplate.postForEntity(
                requestUrl,
                requestBody,
                String.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }
}
