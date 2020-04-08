// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime.web.service;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.ExtensionsV1beta1IngressList;
import io.kubernetes.client.models.ExtensionsV1beta1IngressRule;
import io.kubernetes.client.models.ExtensionsV1beta1IngressSpec;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.dolittle.ingress.uptime.web.util.UptimeConstants.INGRESS_MONITORING_LABEL;

@Service
@Slf4j
public class KubernetesService {

    private final ExtensionsV1beta1Api extV1beta1Api;

    public KubernetesService() {
        try {

            ApiClient apiClient = ClientBuilder.cluster().build();
            Configuration.setDefaultApiClient(apiClient);

        } catch (IOException e) {
            log.error("Unable to initiate k8 client", e);
        }

        extV1beta1Api = new ExtensionsV1beta1Api();
    }

    public List<String> getAllHostToPingFromIngress() {
        List<String> hosts = new ArrayList<>();
        try {
            ExtensionsV1beta1IngressList extensionsV1beta1IngressList = extV1beta1Api.listIngressForAllNamespaces(null, null, INGRESS_MONITORING_LABEL, null, null, null, null, null);
            List<ExtensionsV1beta1IngressSpec> specs = extensionsV1beta1IngressList.getItems().stream().map(ExtensionsV1beta1Ingress::getSpec).collect(Collectors.toList());
            List<ExtensionsV1beta1IngressRule> rules = specs.stream().flatMap(ingressSpec -> ingressSpec.getRules().stream()).collect(Collectors.toList());
            rules.forEach(ingressRule -> {
                log.info("Ingress host: {}", ingressRule.getHost());
                hosts.add(ingressRule.getHost());
            });
        } catch (ApiException e) {
            log.error("Unable to list ingress for all namespaces", e);
        }
        return hosts;
    }
}
