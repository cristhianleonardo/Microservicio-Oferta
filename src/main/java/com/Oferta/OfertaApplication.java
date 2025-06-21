package com.Oferta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class OfertaApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMalformed()
			.ignoreIfMissing()
			.load();

		// Solo setea si no existen en el entorno
		setEnvIfMissing("DB_USERNAME", dotenv.get("DB_USERNAME"));
		setEnvIfMissing("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		setEnvIfMissing("DB_URL", dotenv.get("DB_URL"));

		SpringApplication.run(OfertaApplication.class, args);
	}

	private static void setEnvIfMissing(String key, String fallbackValue) {
		if (System.getenv(key) == null && System.getProperty(key) == null && fallbackValue != null) {
			System.setProperty(key, fallbackValue);
		}
	}
}
