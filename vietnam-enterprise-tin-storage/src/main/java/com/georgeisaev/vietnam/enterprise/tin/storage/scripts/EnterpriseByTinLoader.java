package com.georgeisaev.vietnam.enterprise.tin.storage.scripts;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class EnterpriseByTinLoader {

	public static void main(String[] args) {

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			for (String tin : args) {
				HttpPost httpPost = new HttpPost("http://localhost:8081/enterprises/" + tin);
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("user", "myPassword");
				httpPost.addHeader(new BasicScheme().authenticate(credentials, httpPost, null));
				CloseableHttpResponse response = client.execute(httpPost);
				log.info("Tin {} sent to process with status code: {}", tin, response.getStatusLine().getStatusCode());
				response.close();
			}
		} catch (IOException | AuthenticationException e) {
			log.error("", e);
		}

	}

}
