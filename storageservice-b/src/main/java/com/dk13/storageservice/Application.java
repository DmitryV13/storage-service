package com.dk13.storageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	//http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {
		// load properties from .env
		//Dotenv dotenv = Dotenv.load();
		
		// set them as system variables
		//dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		
		SpringApplication.run(Application.class, args);
	}

}
