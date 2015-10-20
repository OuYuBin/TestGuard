package com.gesila.test.guard.application.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

/**
 * 
 * @author robin
 *
 */
public class GesilaGetHttpExecute extends GesilaHttpExecute {

	public GesilaGetHttpExecute(IGesilaHttpClient gesilaHttpClient) {
		super(gesilaHttpClient);
	}

	public HttpResponse execute() {
		String url = (String) gesilaHttpClient.getUrl();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader(HTTP.CONTENT_TYPE, "application/json");
		try {
			GesilaHttpClientContext gesilaHttpClientContext;
			if (GesilaCookie.getInstance().gesilaHttpClientContext != null) {
				gesilaHttpClientContext = GesilaCookie.getInstance().gesilaHttpClientContext;
			} else {
				gesilaHttpClientContext = new GesilaHttpClientContext();
			}
			HttpResponse response = gesilaHttpClient.getHttpClient().execute(httpGet, gesilaHttpClientContext);
			if (200 == response.getStatusLine().getStatusCode()) {
				createCookieStore(gesilaHttpClientContext);
			}
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
