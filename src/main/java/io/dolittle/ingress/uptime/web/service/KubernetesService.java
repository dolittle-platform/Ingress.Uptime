// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.service;

import io.dolittle.ingress.uptime.web.model.PingHost;
import io.dolittle.ingress.uptime.web.properties.MonitorProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class KubernetesService {

    private final ExtensionsV1beta1Api extV1beta1Api;
    private final MonitorProperties properties;

    @Autowired
    public KubernetesService(MonitorProperties properties) {
        this.properties = properties;
        try {

            ApiClient apiClient = ClientBuilder.cluster().build();
            Configuration.setDefaultApiClient(apiClient);

        } catch (IOException e) {
            log.error("Unable to initiate k8 client", e);
        }

        extV1beta1Api = new ExtensionsV1beta1Api();
    }

    public List<PingHost> getAllHostToPingFromIngress() {
        List<PingHost> hosts = new ArrayList<>();
        try {
            ExtensionsV1beta1IngressList extensionsV1beta1IngressList = extV1beta1Api.listIngressForAllNamespaces(null, null, null, properties.getIngressSelector(), null, null, null, null, null);
            if (extensionsV1beta1IngressList != null) {

                List<ExtensionsV1beta1IngressSpec> specs = extensionsV1beta1IngressList.getItems().stream().map(ExtensionsV1beta1Ingress::getSpec).collect(Collectors.toList());
                specs.forEach(ingressSpec -> {
                    PingHost pingHost = new PingHost();

                    ExtensionsV1beta1IngressRule ingressRule = ingressSpec.getRules().get(0);
                    pingHost.setHost(ingressRule.getHost());
                    pingHost.setPath(ingressRule.getHttp().getPaths().get(0).getPath());
                    log.debug("Ingress host: {}, path: {}", pingHost.getHost(), pingHost.getPath());

                    List<ExtensionsV1beta1IngressTLS> tls = ingressSpec.getTls();
                    pingHost.setTls(tls != null);
                    log.debug("Adding ping URL: {}", pingHost.getURL());
                    hosts.add(pingHost);
                });
            }
        } catch (ApiException e) {
            log.error("Unable to list ingress for all namespaces", e);
        }
        return hosts;
    }
}
