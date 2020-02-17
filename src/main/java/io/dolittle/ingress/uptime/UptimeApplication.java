package io.dolittle.ingress.uptime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.dolittle.ingress.uptime.config.web"})
@Slf4j
public class UptimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UptimeApplication.class, args);
		log.info("************ Ingress Uptime STARTED **************");
	}

}
