package br.com.jpb.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.base.Charsets;

public class IntegrationUtil {

	public static String httpGet(String url, Map<String, String> parameters)
			throws IOException {

		StringBuilder urlWithParams = new StringBuilder();
		urlWithParams.append(url);
		for (String param : parameters.keySet()) {
			urlWithParams.append("&");
			urlWithParams.append(param);
			urlWithParams.append("=");
			urlWithParams.append(parameters.get(param));
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(urlWithParams.toString().replaceFirst(
				"&", "?"));

		HttpResponse response = client.execute(request);

		return IOUtils.toString(response.getEntity().getContent(),
				Charsets.UTF_8.toString());
	}
}