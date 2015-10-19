package com.gesila.test.guard.application.http;

/**
 * 
 * @author robin
 *
 */
public class GesilaHttpClientUtil {

	public static Object execute(IGesilaHttpClient gesilaHttpClient) {
		IGesilaHttpExecute httpExectue = createExectue(gesilaHttpClient);
		return httpExectue.execute();
	}

	private static IGesilaHttpExecute createExectue(IGesilaHttpClient gesilaHttpClient) {
		RequestType requestType = gesilaHttpClient.getRequestType();
		IGesilaHttpExecute httpExectue = null;
		switch (requestType) {
		case POST:
			httpExectue = new GesilaPostHttpExecute(gesilaHttpClient);
			break;
		case GET:
			httpExectue=new GesilaGetHttpExecute(gesilaHttpClient);
			break;
		}
		return httpExectue;
	}

}
