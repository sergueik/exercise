package example.controller;

// import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Assumptions;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "serverPort=8085" })
@PropertySource("classpath:application.properties")

public class AcceptancePostTest {

	@Value("${example.host}")
	private String shorthost;

	@LocalServerPort
	private int randomServerPort = 8085;
	private final Gson gson = new Gson();
	// NOTE: exercising property file override
	// NOTE: will not resolve shorthost when initialized here
	// private String body = String.format("{\"result\":\"%s/JD+mPIWm\"}",
	// shorthost);
	private final RestTemplate restTemplate = new RestTemplate();
	private String url = null;
	private String json = null;
	private final Map<String, String> data = new HashMap<>();
	private HttpHeaders headers = new HttpHeaders();

	@BeforeEach
	public void setUp() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	public void encodeTest() {
		String key = "xxxx";
		data.clear();
		data.put("url", key);
		// NOTE: REST end points trailing slashes Java 17
		url = "http://localhost:" + randomServerPort + "/encode";

		final HttpEntity<String> request = new HttpEntity<String>(gson.toJson(data), headers);

		// NOTE: failure to serialize the payload will manifest in runtime
		// request = new HttpEntity<String>(data.toString(), headers);

		// ReadableException: JSON parse error:
		// Unexpected character ('u' (code 117)):
		// was expecting double-quote to start field name

		final ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class, headers);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		final String body = String.format("{\"result\":\"%s/JD+mPIWm\"}", shorthost);
		assertThat(responseEntity.getBody(), containsString(body));
	}

	@Test
	public void decodeTest() {
		final String key = "xxxx";
		final String value = shorthost + "/" + "JD+mPIWm";
		data.clear();
		data.put("url", key);

		// NOTE: REST end points trailing slashes Java 17
		url = "http://localhost:" + randomServerPort + "/encode";
		@SuppressWarnings("rawtypes")
		final HttpEntity<Map> request = new HttpEntity<Map>(data, headers);
		final ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class, headers);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		final Map responseData = responseEntity.getBody();
		assertThat(responseData.containsKey("result"), is(true));
		assertThat(responseData.get("result"), is(value));
	}

	@Test
	public void missingDataJSONTest() {
		url = "http://localhost:" + randomServerPort + "/decode";
		json = "{\"dummy\": \"value\"}";
		Exception exception = assertThrows(RestClientException.class, () -> {
			final HttpEntity<String> request = new HttpEntity<String>(json, headers);
			restTemplate.postForEntity(url, request, String.class, headers);
		});
		assertThat(exception.getMessage(), containsString(Integer.toString(HttpStatus.NOT_IMPLEMENTED.value())));
	}

	@Test
	public void badEndPointTest() {
		url = "http://localhost:" + randomServerPort + "/decode/something";
		json = "{\"url\": \"value\"}";
		Exception exception = assertThrows(RestClientException.class, () -> {
			final HttpEntity<String> request = new HttpEntity<String>(json, headers);
			restTemplate.postForEntity(url, request, String.class, headers);
		});
		assertThat(exception.getMessage(), containsString(Integer.toString(HttpStatus.METHOD_NOT_ALLOWED.value())));
	}

	@Test
	public void malformedShortUrlTest() {
		json = "{\"url\": \"badurl\"}";
		url = "http://localhost:" + randomServerPort + "/decode";
		Exception exception = assertThrows(RestClientException.class, () -> {
			final HttpEntity<String> request = new HttpEntity<String>(json, headers);
			restTemplate.postForEntity(url, request, String.class, headers);
		});
		assertThat(exception.getMessage(), containsString(Integer.toString(HttpStatus.NOT_FOUND.value())));
	}

}
