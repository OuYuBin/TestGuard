package com.gesila.test.guard.application.parts;

import com.gesila.test.guard.application.http.RequestType;

/**
 * 
 * @author robin
 *
 */
public class TestGuardUrlObject extends TestGuardObject {

	private String url;

	private RequestType requestType;
	
	private String requestBody;

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public TestGuardUrlObject(String name) {
		super(name);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
