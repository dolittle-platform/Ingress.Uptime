// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.rest;

import io.dolittle.ingress.uptime.web.model.Response;
import io.dolittle.ingress.uptime.web.util.UptimeConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PingController {

    private final Environment environment;

    public PingController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(value = "/ping")
    public Response Ping(@RequestHeader(value = UptimeConstants.CHALLENGE_KEY) String verificationCode, HttpServletResponse httpServletResponse) {

        String responseKey = DigestUtils.md5Hex(environment.getProperty(UptimeConstants.PROPERTY_HASH_SALT) + verificationCode);

        httpServletResponse.setHeader(UptimeConstants.RESPONSE_KEY, responseKey);
        return Response.ok(responseKey);
    }

}
