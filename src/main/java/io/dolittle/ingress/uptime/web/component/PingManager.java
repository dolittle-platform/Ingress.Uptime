// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.component;

import io.dolittle.ingress.uptime.web.model.PingStatus;
import io.dolittle.ingress.uptime.web.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PingManager {

    private final IngressManager ingressManager;
    private final RequestService requestService;
    private final PingStatus pingStatus = new PingStatus();

    @Autowired
    public PingManager(RequestService requestService, IngressManager ingressManager) {
        this.ingressManager = ingressManager;
        this.requestService = requestService;
        log.info("Ping Manager instantiated.");
    }

    @Scheduled(cron = "0 3/5 * * * ?")
    public void doPing() {
        List<String> hostList = ingressManager.getHostList();

        hostList.forEach(host -> {
            requestService.pingHost(host).thenAcceptAsync(map -> {
                Boolean status = map.get(host);
                pingStatus.updateStatus(host, status);
                log.debug("Done pinging: {}", host);
            });
        });
    }

    public Boolean getStatus() {
        Boolean status = pingStatus.getStatus();
        if (!status) {
            printStatus();
        }
        return status;
    }

    public void printStatus() {
        pingStatus.print();
    }
}
