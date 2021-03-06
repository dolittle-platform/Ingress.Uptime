// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.model;

import lombok.Data;

import static io.dolittle.ingress.uptime.web.util.UptimeConstants.PROTOCOL_HTTP;
import static io.dolittle.ingress.uptime.web.util.UptimeConstants.PROTOCOL_HTTPS;

@Data
public class PingHost {
    private String host;
    private String path;
    private Boolean tls;

    public String getURL() {
        String url ;
        if (tls) {
            url = PROTOCOL_HTTPS;
        } else {
            url = PROTOCOL_HTTP;
        }
        return url + host + path;
    }
}
