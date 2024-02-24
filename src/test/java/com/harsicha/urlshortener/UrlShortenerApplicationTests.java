package com.harsicha.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsicha.urlshortener.abstracts.UrlService;
import com.harsicha.urlshortener.dto.Url;
import com.harsicha.urlshortener.exception.UrlNotValidException;
import com.harsicha.urlshortener.exception.ValidStringNotFoundException;
import com.harsicha.urlshortener.model.UrlMapping;
import com.harsicha.urlshortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UrlShortenerApplicationTests {

	private static final String URL = "https://www.google.com";
	private static final String RANDOM = "8ffdef";

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, @Qualifier("v2") UrlService urlService) throws ValidStringNotFoundException, NoSuchAlgorithmException, UrlNotValidException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		urlService.saveUrl(URL);
	}

	@Test
	void postShouldReturnOk() throws Exception {
		Url url = new Url();
		url.setUrl(URL);
		this.mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(url)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(x -> content().string(containsString(RANDOM)));
	}

	@Test
	void getShouldReturnOk() throws Exception {
		String url = "/" + RANDOM;
		this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isTemporaryRedirect());
	}

	@Test
	void postShouldReturnUrlNotValidBadRequest() throws Exception {
		Url url1 = new Url();
		url1.setUrl("htlps://www.google.com");
		Url url2 = new Url();
		url1.setUrl("https://ww.google.com");
		Url url3 = new Url();
		url1.setUrl("John Doe");

		List<Url> invalidUrls = new ArrayList<>();
		invalidUrls.add(url1);
		invalidUrls.add(url2);
		invalidUrls.add(url3);

		for (Url url : invalidUrls) {
			this.mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(url)))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(content().string(containsString("Please enter a valid url")));
		}
	}

}
