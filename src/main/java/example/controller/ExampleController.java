package example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import example.service.ExampleService;

@RestController
@RequestMapping("/basic")
public class ExampleController {

	private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);


	private static Gson gson = new GsonBuilder().create();
	private boolean debug = false;
	// see also about writing SpringBoot application tests without relying on
	// SpringBoot field injection
	// https://reflectoring.io/unit-testing-spring-boot/

	@Autowired
	private ExampleService service;

	@Autowired
	public ExampleController(ExampleService data) {
		service = data;
	}

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
	public ResponseEntity<Map<String, String>> decode(@PathVariable("url") String url) {
		Map<String, String> result = new HashMap<>();
		String decoded = service.decode(url);
		result.put("result", decoded);
		return ResponseEntity.status(HttpStatus.OK).body(result);

	}

	public ExampleController() {

	}

}
