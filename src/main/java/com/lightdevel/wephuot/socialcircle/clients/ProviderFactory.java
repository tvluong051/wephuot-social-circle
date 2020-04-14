package com.lightdevel.wephuot.socialcircle.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProviderFactory {
	private Map<String, Provider> supportedProviders;

	@Autowired
	public ProviderFactory(FacebookClient facebookClient) {
		this.supportedProviders = new HashMap<>();
		this.supportedProviders.put("facebook", facebookClient);
	}


	public Provider getProvider(String name) {
		return this.supportedProviders.get(name);
	}
}
