package com.gesila.test.guard.application.http;

import org.apache.http.HttpResponse;

/**
 * 
 * @author robin
 *
 */
public class GesilaPostHttpExecute extends GesilaHttpExecute {
	
	
	public GesilaPostHttpExecute(IGesilaHttpClient gesilaHttpClient) {
		super(gesilaHttpClient);
	}

	@Override
	public HttpResponse execute() {
		return null;
	}
}
