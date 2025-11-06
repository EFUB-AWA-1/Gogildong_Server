package com.efub.gogildong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GogildongApplication {

	public static void main(String[] args) {
		SpringApplication.run(GogildongApplication.class, args);
	}

}
