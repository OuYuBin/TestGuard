package com.gesila.test.guard.application.http;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * 
 * @author robin
 *
 */
public class GesilaCookie {

	public static GesilaCookie INSTANCE = new GesilaCookie();

	public static BasicCookieStore cookieStore = new BasicCookieStore();

	public static GesilaHttpClientContext gesilaHttpClientContext;

	private GesilaCookie() {

	}

	public static GesilaCookie getInstance() {
		return INSTANCE;
	}

	public synchronized void createCookie(GesilaHttpClientContext gesilaHttpClientContext) {
		// String cookieValue = header.getValue();
		// String JSESSIONID = cookieValue.substring("JSESSIONID=".length(),
		// cookieValue.indexOf(";"));
		// BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
		// JSESSIONID);
		// cookieStore.addCookie(cookie);
		if (this.gesilaHttpClientContext == null) {
			this.gesilaHttpClientContext = gesilaHttpClientContext;
		}
	}

	public static BasicCookieStore getCookieStore() {
		return cookieStore;
	}

}
