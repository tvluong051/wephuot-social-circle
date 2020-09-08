package com.lightdevel.wephuot.socialcircle.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class FacebookClient implements Provider {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookClient.class);

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class TokenData {
		@JsonProperty("is_valid")
		private boolean valid;

		@JsonProperty("user_id")
		private String userId;
	}
	@Value("${identity-providers.facebook.url}")
	private String FACEBOOK_API_BASE_URL;

	@Value("${identity-providers.facebook.client-id}")
	private String clientId;

	@Value("${identity-providers.facebook.client-secret}")
	private String clientSecret;

	private static final String DEBUG_TOKEN_PATH = "/debug_token";

	private RestTemplate restTemplate;

	@Autowired
	public FacebookClient(RestTemplate restTemplate) {
		this.restTemplate = Objects.requireNonNull(restTemplate);
	}

	@Override
	public String getProvidedIdFromToken(String token) {
		ResponseEntity<String> response = restTemplate.getForEntity(String.format(FACEBOOK_API_BASE_URL + DEBUG_TOKEN_PATH + "?input_token=%s&access_token=%s|%s", token, clientId, clientSecret), String.class);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, TokenData> body = mapper.readValue(response.getBody(), new TypeReference<Map<String, TokenData>>(){});
			TokenData data = body.get("data");
			if (data.isValid()) {
				return data.getUserId();
			}
		} catch (IOException e) {
			LOGGER.error("Problem getting providedId from token with facebook");
		}
		return null;
	}
}
