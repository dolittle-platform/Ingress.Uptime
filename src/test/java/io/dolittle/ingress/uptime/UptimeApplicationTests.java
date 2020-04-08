// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.ingress.uptime;

import io.dolittle.ingress.uptime.web.component.IngressManager;
import io.dolittle.ingress.uptime.web.component.PingManager;
import io.dolittle.ingress.uptime.web.service.KubernetesService;
import io.dolittle.ingress.uptime.web.service.RequestService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UptimeApplicationTests {
	@Mock
	KubernetesService kubernetesService;

	@Mock
	RequestService requestService;

	@Mock
	IngressManager ingressManager;

	@InjectMocks
	PingManager pingManager = new PingManager(requestService, ingressManager);

	@Test
	void contextLoads() {
	}

}
