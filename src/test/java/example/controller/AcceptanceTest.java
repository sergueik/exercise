package example.controller;
/**
 * Copyright 2021,2022,2024 Serguei Kouzmine
 */
// import org.junit.Before;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.HttpStatus;

import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "serverPort=8085" })
@PropertySource("classpath:application.properties")

public class AcceptanceTest {

	@LocalServerPort
	private int randomServerPort = 8085;

	private final String route = "/basic";
	// NOTE: exercising property file override
	private static String body = "{\"result\":\"JD+mPIWm\"}";
	private static final RestTemplate restTemplate = new RestTemplate();
	private String url = null;
	private ResponseEntity<String> responseEntity = null;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void test1() throws Exception {
		Assumptions.assumeFalse(false);
		url = "http://localhost:" + randomServerPort + route + "/encode/xxxx";
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		body = "{\"result\":\"JD+mPIWm\"}";
		assertThat("Unexpectedresponse for " + route, responseEntity.getBody(), is(body));
	}

	@Test
	public void test2() throws Exception {
		Assumptions.assumeFalse(false);
		url = "http://localhost:" + randomServerPort + route + "/decode/JD+mPIWm";
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		body = "{\"result\":\"xxxx\"}";
		assertThat("Unexpectedresponse for " + route, responseEntity.getBody(), is(body));
	}

}
