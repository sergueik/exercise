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

		if (fullUrlCache.containsKey(name)) {
			logger.info("using fullUrlCache for {}", name);
			value = fullUrlCache.get(name);
		} else {
			try {
				messageDigest.update(name.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {

				return null;
			}
			String hash = new String(messageDigest.digest());

			do {
				// NOTE: some base64 short urls e.g. the "JD+mPIWm" is likely
				// *not* URI clean.
				// https://en.wikipedia.org/wiki/Base62
				value = new String(standardEncoder.encode(hash.getBytes())).substring(0, 8);
			} while (shortUrlCache.containsKey(value));
			fullUrlCache.put(name, value);
			shortUrlCache.put(value, name);
			logger.info("saving caches for {}, {}", name, value);
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
