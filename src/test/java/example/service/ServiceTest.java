package example.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@SpringBootTest
@PropertySource("classpath:application.properties")

public class ServiceTest {

	@Autowired
	private ExampleService service;

	@ParameterizedTest
	@MethodSource("testData")
	public void encodeTest(String hostname, String shortName) {
		assertThat(service.encode(hostname), is(shortName));
	}

	// App does not persist short URLs to a database - Keeps them in memory
	@Test
	public void decodeMissingShortNameTest() {
		assertThat(service.decode("anything"), nullValue());
	}

	@ParameterizedTest
	@MethodSource("testData")
	public void decodeTest(String hostname) {
		String shortName = service.encode(hostname);
		assertThat(service.decode(shortName), is(hostname));
	}

	public static Object[][] testData() {
		return new Object[][] { { "xxxx", "8avk3Ax0" }, { "https://www.google.com/", "nWzeJanK" },
				// TODO: handle blank hostnames better ?
				{ "", "rzWtdMwJ" }, };
	}

}
