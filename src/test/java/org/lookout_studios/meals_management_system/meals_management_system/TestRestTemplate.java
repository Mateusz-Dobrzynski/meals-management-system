package org.lookout_studios.meals_management_system.meals_management_system;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * This is an extension of RestTemplate class which does not throw exceptions
 * when
 * http responses are not OK (200)
 */
public class TestRestTemplate extends RestTemplate {
    public TestRestTemplate() {
        this.setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });
        this.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}
