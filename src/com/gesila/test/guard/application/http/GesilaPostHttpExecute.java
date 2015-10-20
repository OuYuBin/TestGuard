package com.gesila.test.guard.application.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

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
		String url = (String) gesilaHttpClient.getUrl();
		HttpPost httpPost = new HttpPost(url);
		try {
			GesilaHttpClientContext gesilaHttpClientContext;
			if (GesilaCookie.getInstance().gesilaHttpClientContext != null) {
				gesilaHttpClientContext = GesilaCookie.getInstance().gesilaHttpClientContext;
			} else {
				gesilaHttpClientContext = new GesilaHttpClientContext();
			}
			String requestJSON = gesilaHttpClient.getRequestJSON();
			if (requestJSON != null) {
				StringEntity se = new StringEntity(requestJSON);
				httpPost.setEntity(se);
			}
			HttpResponse response = gesilaHttpClient.getHttpClient().execute(httpPost, gesilaHttpClientContext);
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
