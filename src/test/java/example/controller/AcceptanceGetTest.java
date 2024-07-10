package example.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

// import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Assumptions;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "serverPort=8085" })
@PropertySource("classpath:application.properties")

public class AcceptanceGetTest {
	@LocalServerPort
	private int randomServerPort = 8085;

	private static String body = "{\"result\":\"JD+mPIWm\"}";
	private final RestTemplate restTemplate = new RestTemplate();
	private String url = null;
	private ResponseEntity<String> responseEntity = null;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void test1() throws Exception {
		final String key = "xxxx";
		final String value = "JD+mPIWm";
		final String responseBody = String.format("{\"result\":\"%s\"}", value);
		url = "http://localhost:" + randomServerPort + "/encode/" + key;
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat("Unexpectedresponse for " + key, responseEntity.getBody(), is(responseBody));
	}

	@Test
	public void test2() throws Exception {
		final String key = "JD+mPIWm";
		final String value = "xxxx";
		final String responseBody = String.format("{\"result\":\"%s\"}", value);
		url = "http://localhost:" + randomServerPort + "/decode/" + key;
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat("Unexpected response for " + key, responseEntity.getBody(), is(responseBody));
	}

	@Test
	public void test3() {
		final String key = "zzz";
		HttpStatus status = HttpStatus.NOT_FOUND;
		url = "http://localhost:" + randomServerPort + "/decode/" + key;
		Exception exception = assertThrows(RestClientException.class, () -> {
			responseEntity = restTemplate.getForEntity(url, String.class);
		});
		assertThat(exception.getMessage(), containsString(Integer.toString(status.value())));
	}

}
