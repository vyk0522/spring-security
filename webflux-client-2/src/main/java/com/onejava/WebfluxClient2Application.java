package com.onejava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableScheduling
public class WebfluxClient2Application implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

	public static void main(String[] args) {
		SpringApplication.run(WebfluxClient2Application.class, args);
	}

	@Autowired
	private WebClient webClient;

	@Scheduled(fixedRate = 5000)
	public void scheduledRequest() {
		webClient.get()
				.uri("http://localhost:8080")
				.retrieve()
				.bodyToMono(String.class)
				.map(string
						-> "Schedule request response: " + string)
				.subscribe(logger::info);
	}

	@Override
	public void run(String... args) throws Exception {
		String body = webClient.get()
				.uri("http://localhost:8080")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		logger.info(body);
	}

}
