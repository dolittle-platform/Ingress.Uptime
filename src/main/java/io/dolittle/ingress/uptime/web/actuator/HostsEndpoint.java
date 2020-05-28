// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import io.dolittle.ingress.uptime.web.component.PingManager;
import io.dolittle.ingress.uptime.web.model.PingStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Endpoint(id = "hosts")
public class HostsEndpoint {

    private final PingManager pingManager;

    @Autowired
    public HostsEndpoint(PingManager pingManager) {
        this.pingManager = pingManager;
    }

    @ReadOperation
    PingStatus status() {
        log.debug("Providing managment interface hosts status");
        return pingManager.getStatus();
    }
}