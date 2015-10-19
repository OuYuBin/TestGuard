package com.gesila.test.guard.application.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 
 * 
 * @author robin
 *
 */
public class GesilaHttpClient implements IGesilaHttpClient {

	// private HttpClient httpClient;

	private Object url;

	private RequestType requestType;

	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public GesilaHttpClient(Object url) {
		this(url, RequestType.GET);
	}

	public GesilaHttpClient(Object url, RequestType requestType) {
		this.url = url;
		this.requestType = requestType;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

}
