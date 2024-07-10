package example.service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class ExampleService {

	private static final Logger logger = LoggerFactory.getLogger(ExampleService.class);

	private final Map<String, String> fullUrlCache = new HashMap<>();
	private final Map<String, String> shortUrlCache = new HashMap<>();
	private MessageDigest messageDigest;
	private Base64 base64;

	public ExampleService() {
		base64 = new Base64();
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
			messageDigest.update(name.getBytes());
			String hash = new String(messageDigest.digest());

			do {
				// NOTE: some short urls e.g. the "JD+mPIWm" is likely *not* a
				// valid URL. Need to URL
				// how to encode ?
				// https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Base64.html
				// https://en.wikipedia.org/wiki/Base62
				value = new String(base64.encodeBase64(hash.getBytes())).substring(0, 8);
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