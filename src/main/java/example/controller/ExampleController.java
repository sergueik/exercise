package example.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import example.service.ExampleService;

@RestController
@RequestMapping("/")
public class ExampleController {

	private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

	private boolean debug = false;

	// @Autowired
	private ExampleService service;

	@Autowired
	public ExampleController(ExampleService data) {
		service = data;
	}

	// TODO: regexp or processing of the url argument
	// NOTE: passing the url through path variable is not good
	@RequestMapping(method = RequestMethod.GET, value = "/encode/{url}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Map<String, String>> encode(@PathVariable("url") String url) {
		Map<String, String> result = new HashMap<>();
		String encoded = service.encode(url);
		result.put("result", encoded);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/decode/{url}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> decode(@PathVariable("url") String url) {
		Map<String, String> result = new HashMap<>();
		String decoded = service.decode(url);
		logger.debug("Processing \"{}\"", decoded);
		if (decoded == null) {
			logger.error("Short URL \"{}\" was not found", url);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			logger.debug("Returning \"{}\"", decoded);
			result.put("result", decoded);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/encode", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Map<String, String>> encode(@RequestBody Map<String, String> payload) {
		logger.debug("processing {}", payload);
		String url = payload.get("url");
		if (url == null) {
			logger.error("invalid payload {}", payload);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
		} else {
			Map<String, String> result = new HashMap<>();
			String encoded = service.encode(url);
			result.put("result", encoded);
			logger.debug("returning {}", result);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
	}

}
