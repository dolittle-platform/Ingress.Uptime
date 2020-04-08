// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.service;

import io.dolittle.ingress.uptime.web.component.KeyManager;
import io.dolittle.ingress.uptime.web.model.Response;
import io.dolittle.ingress.uptime.web.util.RESTUtil;
import io.dolittle.ingress.uptime.web.util.UptimeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static io.dolittle.ingress.uptime.web.util.UptimeConstants.PATH_PING;
import static io.dolittle.ingress.uptime.web.util.UptimeConstants.PROTOCOL_HTTPS;

@Service
@Slf4j
public class RequestService {

    private final KeyManager keyManager;

    @Autowired
    public RequestService(KeyManager keyManager) {

        this.keyManager = keyManager;
    }

    @Async
    public CompletableFuture<HashMap<String, Boolean>> pingHost(String host) {
        HashMap<String, Boolean> result = new HashMap<>();
        result.put(host, Boolean.FALSE);

        String url = PROTOCOL_HTTPS + host + PATH_PING;

        String challengeKey = keyManager.addChallengeKey(host);

        RestTemplate restTemplate = RESTUtil.getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(UptimeConstants.CHALLENGE_KEY, challengeKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Response> exchange;
        try {
            log.debug("Pinging: {}, challenge-key: {}", url, challengeKey);
            exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Response.class);

        } catch (RestClientException e) {
            log.error("Error pinging: "+ url, e);
            return CompletableFuture.completedFuture(result);
        }

        Response response = exchange.getBody();
        assert response != null;

        if (!verifyResponseKey(response, host)) {
            return CompletableFuture.completedFuture(result);
        }

        result.put(host, Boolean.TRUE);

        return CompletableFuture.completedFuture(result);
    }

    private Boolean verifyResponseKey(Response response, String host) {
        Response.Status status = response.getStatus();
        String responsekey = response.getResponsekey();

        log.debug("Got response: Status - {}, Response-key: {}", status, responsekey);

        if (status.equals(Response.Status.OK)) {
            if (responsekey == null || responsekey.isEmpty()) {
                return false;
            }
            return keyManager.verifyResponseKey(host, responsekey);
        }
        return false;
    }
}
