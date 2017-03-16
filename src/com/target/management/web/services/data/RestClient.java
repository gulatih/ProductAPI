package com.target.management.web.services.data;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {

	private static RestClient instance;
	private static final String JSON = "application/json";

	/**
	 * 
	 * @return instance of class
	 */
	public static RestClient getInstance() {
		if (instance == null) {
			synchronized (RestClient.class) {
				if (instance == null) {
					instance = new RestClient();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param url
	 * @return ClientResponse
	 */
	public ClientResponse connect(String url) {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept(JSON).get(ClientResponse.class);
		return response;
	}
}
