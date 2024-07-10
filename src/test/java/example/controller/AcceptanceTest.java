package example.controller;

// import static org.hamcrest.Matchers.containsString;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
		url = "http://localhost:" + randomServerPort + route + "/encode/xxxx";
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		body = "{\"result\":\"JD+mPIWm\"}";
		assertThat("Unexpectedresponse for " + route, responseEntity.getBody(), is(body));
	}

	@Test
	public void test2() throws Exception {
		url = "http://localhost:" + randomServerPort + route + "/decode/JD+mPIWm";
		responseEntity = restTemplate.getForEntity(url, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		body = "{\"result\":\"xxxx\"}";
		assertThat("Unexpectedresponse for " + route, responseEntity.getBody(), is(body));
	}

	@Test
	public void test3() {
		for (HttpStatus status : Arrays.asList(HttpStatus.NOT_FOUND)) {
			url = "http://localhost:" + randomServerPort + route + "/decode/zzz";
			Exception exception = assertThrows(RestClientException.class, () -> {
				responseEntity = restTemplate.getForEntity(url, String.class);
			});
			assertThat(exception.getMessage(), containsString(Integer.toString(status.value())));
		}
	}

}
