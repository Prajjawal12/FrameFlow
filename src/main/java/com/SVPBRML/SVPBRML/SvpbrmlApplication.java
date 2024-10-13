package com.SVPBRML.SVPBRML;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SvpbrmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvpbrmlApplication.class, args);
	}

}
