package com.gesila.test.guard.application.http;

import org.apache.http.HttpResponse;

/**
 * 
 * @author robin
 *
 */
public abstract class GesilaHttpExecute implements IGesilaHttpExecute {

	protected IGesilaHttpClient gesilaHttpClient;

	public GesilaHttpExecute(IGesilaHttpClient gesilaHttpClient) {
		this.gesilaHttpClient = gesilaHttpClient;
	}

	public abstract HttpResponse execute();

	protected void createCookieStore(GesilaHttpClientContext clientContext) {
		//org.apache.http.Header header = response.getFirstHeader("Set-Cookie");
		GesilaCookie.getInstance().createCookie(clientContext);
	}

}
