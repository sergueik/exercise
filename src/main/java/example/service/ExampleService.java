package example.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.seruco.encoding.base62.Base62;

@Service
public class ExampleService {

	private static final Logger logger = LoggerFactory.getLogger(ExampleService.class);

	private final Map<String, String> fullUrlCache = new HashMap<>();
	private final Map<String, String> shortUrlCache = new HashMap<>();
	private MessageDigest messageDigest;
	private Base62 standardEncoder;

	public ExampleService() {
		standardEncoder = Base62.createInstance();
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (Exception e) {
			// swallow
		}
	}

	public String encode(final String name) {
		String value = null;
		try {

			if (fullUrlCache.containsKey(name)) {
				logger.info("using fullUrlCache for {}", name);
				value = fullUrlCache.get(name);
			} else {
				// https://www.baeldung.com/string/get-bytes
				messageDigest.update(name.getBytes("UTF-8"));
				final byte[] digest = messageDigest.digest();

				do {
					// NOTE: Base64 produces
					// an URI-unfrienly data e.g. the "JD+mPIWm" or ""
					// need base62 to encode
					// https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Base64.html
					// https://en.wikipedia.org/wiki/Base62
					value = new String(standardEncoder.encode(digest), "UTF-8").substring(0, 8);
					logger.info("shortname for {}, {}", name, value);
					if (shortUrlCache.containsKey(value)) {
						logger.info("shortname {} is alresdy int the cache!", value);
					}
				} while (shortUrlCache.containsKey(value));
				fullUrlCache.put(name, value);
				shortUrlCache.put(value, name);
				logger.info("saving caches for {}, {}", name, value);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Bad encoding in {}", name);
			return null;
		}
		return value;
	}

	public String decode(final String value) {
		String name = null;
		if (shortUrlCache.containsKey(value)) {
			name = shortUrlCache.get(value);
		} else {
			// TODO: add log4j2.xml or logback.xml and replace info with debug
			logger.error("{} not found in shortUrlCache", value);
			// error to be processed by controller
		}
		return name;
	}
}

