package example.service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

	private static final Logger logger = LoggerFactory.getLogger(ExampleService.class);

	// private static final Map<String,String> cache;
	private final Map<String, String> cache = new HashMap<>();
	private MessageDigest messageDigest;
	private static Base64 base64;

	static {
		base64 = new Base64();
	}

	public ExampleService() {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			// swallo
		}

	}

	public String encode(final String name) {
		String value = null;

		if (cache.containsKey(name)) {
			logger.info("using cache for {}", name);
			value = cache.get(name);
		} else {
			messageDigest.update(name.getBytes());
			String hash = new String(messageDigest.digest());

			do {
				// NOTE: the "JD+mPIWm" is likely *not* a valid URL. Need to URL
				// ecnode
				// https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Base64.html
				value = new String(base64.encodeBase64(hash.getBytes())).substring(0, 8);
			} while (cache.containsKey(value));
			cache.put(name, value);
			cache.put(value, name);
			logger.info("saving cache for {}, {}", name, value);
		}
		return value;
	}

	public String decode(final String value) {
		String name = null;
		if (cache.containsKey(value)) {
			name = cache.get(value);
		} else {
			logger.info("{} not found in cache", value);
			// error to be processed by controller
		}
		return name;
	}

}