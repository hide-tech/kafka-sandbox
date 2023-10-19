package com.ex.function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@SpringBootApplication
public class FunctionApplication {

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(Duration.ofSeconds(20));
		SpringApplication.run(FunctionApplication.class, args);
	}

}
