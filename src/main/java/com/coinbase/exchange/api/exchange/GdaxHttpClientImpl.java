package com.coinbase.exchange.api.exchange;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Service
public class GdaxHttpClientImpl implements GdaxHttpClient {

    private final String publicKey;
    private final String passphrase;
    private final String baseUrl;

    private final Signature signature;
    private final RestTemplate restTemplate;

    @Autowired
    public GdaxHttpClientImpl(@Value("${gdax.key}") String publicKey,
            @Value("${gdax.passphrase}") String passphrase,
            @Value("${gdax.api.baseUrl}") String baseUrl,
            Signature signature,
            RestTemplate restTemplate) {
        this.publicKey = publicKey;
        this.passphrase = passphrase;
        this.baseUrl = baseUrl;
        this.signature = signature;
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T get(String resourcePath, ParameterizedTypeReference<T> responseType) {
        try {
            final ResponseEntity<T> responseEntity = restTemplate.exchange(getBaseUrl() + resourcePath,
                    GET, securityHeaders(resourcePath, "GET", ""), responseType);

            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("GET request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());

            throw new RuntimeException("Failed get request for '" + resourcePath + "'!", ex);
        }
    }

    @Override
    public <T> List<T> getAsList(String resourcePath, ParameterizedTypeReference<T[]> responseType) {
        final T[] result = get(resourcePath, responseType);

        return result == null ? Collections.emptyList() : Arrays.asList(result);
    }

    @Override
    public <T> T pagedGet(String resourcePath,
            ParameterizedTypeReference<T> responseType,
            String beforeOrAfter,
            Integer pageNumber,
            Integer limit) {
        resourcePath += "?" + beforeOrAfter + "=" + pageNumber + "&limit=" + limit;
        return get(resourcePath, responseType);
    }

    @Override
    public <T> List<T> pagedGetAsList(String resourcePath, ParameterizedTypeReference<T[]> responseType,
            String beforeOrAfter, Integer pageNumber, Integer limit) {
        T[] result = pagedGet(resourcePath, responseType, beforeOrAfter, pageNumber, limit);
        return result == null ? Collections.emptyList() : Arrays.asList(result);
    }

    @Override
    public <T> T delete(String resourcePath, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(getBaseUrl() + resourcePath,
                    HttpMethod.DELETE,
                    securityHeaders(resourcePath, "DELETE", ""),
                    responseType);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("DELETE request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }
        return null;
    }

    @Override
    public <T, R> T post(String resourcePath, ParameterizedTypeReference<T> responseType, R jsonObj) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(jsonObj);

        try {
            ResponseEntity<T> response = restTemplate.exchange(getBaseUrl() + resourcePath,
                    HttpMethod.POST,
                    securityHeaders(resourcePath, "POST", jsonBody),
                    responseType);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("POST request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }
        return null;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public HttpEntity<String> securityHeaders(String endpoint, String method, String jsonBody) {
        final HttpHeaders headers = new HttpHeaders();

        final String timestamp = String.valueOf(Instant.now().getEpochSecond());
        final String resource = endpoint.replace(getBaseUrl(), "");

        headers.add("accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("CB-ACCESS-KEY", publicKey);
        headers.add("CB-ACCESS-SIGN", signature.generate(resource, method, jsonBody, timestamp));
        headers.add("CB-ACCESS-TIMESTAMP", timestamp);
        headers.add("CB-ACCESS-PASSPHRASE", passphrase);

        curlRequest(method, jsonBody, headers, resource);

        return new HttpEntity<>(jsonBody, headers);
    }

    private void curlRequest(String method, String jsonBody, HttpHeaders headers, String resource) {
        StringBuilder curlTest = new StringBuilder("curl ");
        for (String key : headers.keySet()) {
            curlTest.append("-H '").append(key).append(":").append(headers.get(key).get(0)).append("' ");
        }
        if (!jsonBody.equals(""))
            curlTest.append("-d '").append(jsonBody).append("' ");

        curlTest.append("-X ").append(method).append(" ").append(getBaseUrl()).append(resource);
        log.debug(curlTest.toString());
    }
}