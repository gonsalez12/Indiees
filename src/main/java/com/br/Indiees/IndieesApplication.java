package com.br.Indiees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class IndieesApplication implements WebMvcConfigurer {
	
	public static void main(String[] args) {
		SpringApplication.run(IndieesApplication.class, args);
	}

}
