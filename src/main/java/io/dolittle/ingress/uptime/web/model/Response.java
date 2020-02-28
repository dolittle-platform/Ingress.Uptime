// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dolittle.ingress.uptime.web.util.UptimeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    private Status status;
    @JsonProperty(value = UptimeConstants.RESPONSE_KEY)
    private String responsekey;

    public static Response ok(String responsekey) {
        return new Response(Status.OK,responsekey);
    }

    public static Response error() {
        return new Response(Status.ERROR,"");
    }

    private enum Status {
        OK,
        ERROR
    }
}
