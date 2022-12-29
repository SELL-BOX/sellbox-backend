package com.prod.sellBox;

import org.kurento.client.KurentoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SellBoxApplication {

	@Bean
	public KurentoClient kurentoClient() {
		return KurentoClient.create();
	}
	public static void main(String[] args) {
		SpringApplication.run(SellBoxApplication.class, args);
	}

}
