package com.br.project.gereme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeremeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeremeApplication.class, args);
	}

}
