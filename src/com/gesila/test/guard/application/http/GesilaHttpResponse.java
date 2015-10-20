package com.gesila.test.guard.application.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * 封装HttpResponse,用于处理已请求过,切换Part后不再HttpResponse对象获取对应流信息
 * 已避免出现httpEntity对象中的内容仅能读取一次造成错误的问题
 * 
 * @author robin
 *
 */
public class GesilaHttpResponse {

	private HttpResponse httpResponse;

	private String strResponse;

	public GesilaHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public String getStrResponse() {
		return strResponse;
	}

	public void setStrResponse(String strResponse) {
		this.strResponse = strResponse;
	}

}
