package com.dk13.storageservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("test")
class ApplicationTests {
	
	@BeforeAll
	static void loadEnv() {
		//Dotenv dotenv = Dotenv.load();
		//dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}
	
	@Test
	void contextLoads() {
		int i =0;
		i++;
	}

}
