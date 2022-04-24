package com.onejava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootApplication
public class WebApplicationClientApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

    public static void main(String[] args) {
        SpringApplication.run(WebApplicationClientApplication.class, args);
    }

    @Autowired
    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    /*
    1. The command line runner method, runs once application is fully started
    2. STEP 1: Retrieve the authorized JWT from Okta server
    3. STEP 2: Use the JWT and call the secured api
     */
    @Override
	public void run(String... args) throws Exception {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("client2").principal("client2-principal").build(); // Build an OAuth2 request for the Okta provider
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest); //JWT is retrieved from the Okta servers.
		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

		logAccessToken(accessToken);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
		HttpEntity request = new HttpEntity(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080", HttpMethod.GET, request, String.class);
		logger.info("responsePayload = " + response.getBody());
	}

	private void logAccessToken(OAuth2AccessToken accessToken) {
		logger.info("Issued: " + accessToken.getIssuedAt().toString() + ", Expires:" + accessToken.getExpiresAt().toString());
		logger.info("Scopes: " + accessToken.getScopes().toString());
		logger.info("Token: " + accessToken.getTokenValue());
	}

}
