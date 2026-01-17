package com.example.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.example.chat")
public class OnlineOpenChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineOpenChatApplication.class, args);
	}

}