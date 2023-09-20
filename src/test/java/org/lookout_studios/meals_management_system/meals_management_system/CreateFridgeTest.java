package org.lookout_studios.meals_management_system.meals_management_system;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class CreateFridgeTest {
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @InjectMocks
    private DatabaseService database;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
        database = mock(DatabaseService.class);
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(8080));

    /**
     * Mocks a scenario of successful fridge creation
     */
    @Test
    public void createNewFridgeSuccess() {
        ResponseEntity<String> response = null;
        String userToken = "1@qwerty";
        String refreshToken = "Q!W@E#r4t5y6";
        String customFridgeName = "Just chillin'";
        String requestPath = "/fridge/create";
        String requestUrl = "http://localhost:8080/fridge/create";
        int userId = 2137;
        JSONObject requestBody = new JSONObject();
        requestBody.put("userId", userId);
        requestBody.put("userToken", userToken);
        requestBody.put("refreshToken", refreshToken);
        requestBody.put("customFridgeName", customFridgeName);

        String responseBody = new ResponseBody(HttpStatus.OK).getResponseBody();

        // TO-DO: Mock a successful token check

        WireMock.stubFor(post(urlEqualTo(requestPath))
                .withRequestBody(WireMock.equalToJson(requestBody.toString()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(responseBody)));

        response = restTemplate.postForEntity(requestUrl, requestBody, String.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    /**
     * Mocks a scenario in which fridge cannot be created due to invalid tokens
     */
    @Test
    public void createFridgeInvalidTokens() {
        ResponseEntity<String> response = null;
        String userToken = "1@qwerty";
        String refreshToken = "Q!W@E#r4t5y6";
        String customFridgeName = "Just chillin'";
        String requestPath = "/fridge/create";
        String requestUrl = "http://localhost:8080/fridge/create";
        int userId = 2137;
        JSONObject requestBody = new JSONObject();
        requestBody.put("userId", userId);
        requestBody.put("userToken", userToken);
        requestBody.put("refreshToken", refreshToken);
        requestBody.put("customFridgeName", customFridgeName);
        String responseBody = new ResponseBody(HttpStatus.UNAUTHORIZED).getResponseBody();

        // TO-DO: Mock a token check that will fail
        WireMock.stubFor(post(urlEqualTo(requestPath))
                .withRequestBody(WireMock.equalToJson(requestBody.toString()))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withBody(responseBody)));

        response = restTemplate.postForEntity(requestUrl, requestBody, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
    }
}
