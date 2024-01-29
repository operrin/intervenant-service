package org.miage.intervenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class IntervenantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntervenantServiceApplication.class, args);
	}

	@Bean
	public OpenAPI intervenantAPI() {
		return new OpenAPI().info(new Info()
			.title("Intervenant API")
			.version("1.0")
			.description("Documentation Intervenant API Miage"));
	}
}
