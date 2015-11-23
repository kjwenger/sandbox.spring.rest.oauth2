package com.u14n.sandbox.spring.rest.service;

import sun.misc.BASE64Encoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import com.jayway.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocationsResourceWithRestAssuredTest {
	private static final int MAX_BUFFER_SIZE = 256; //Maximal size of the chars

	private static Server server;

	/**
	 * @throws Exception
	 * @see <a href="http://stackoverflow.com/questions/5267423/using-a-jetty-server-with-junit-tests">Using a Jetty Server with JUnit tests</a>
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		server = new Server(8080);
		server.setStopAtShutdown(true);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setClassLoader(
				LocationsResourceWithRestAssuredTest.class.getClassLoader());
		server.addHandler(webAppContext);
		server.start();
	}

	/**
	 * @throws Exception
	 * @see <a href="http://localhost:8080/oauth/token?grant_type=password&client_id=my-trusted-client&username=admin&password=password"></a>
	 */
	@Test
	public void shouldGetRefreshToken() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet("http:"
				+ "//localhost"
				+ ":8080"
				+ "/oauth/token"
				+ "?grant_type=password"
				+ "&client_id=my-trusted-client"
				+ "&username=admin"
				+ "&password=password");
//		BASE64Encoder encoder = new BASE64Encoder();
//		String encoded = encoder.encode("admin:password".getBytes());
//		request.setHeader("Authorization", "Basic " + encoded);
		request.setHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
		HttpResponse response = client.execute(request);
//		assertEquals(200, response.getStatusLine().getStatusCode());
		Reader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		char[] chars = new char[MAX_BUFFER_SIZE];
		int read;
		while ((read = reader.read(chars, 0, MAX_BUFFER_SIZE)) > 0) {
			builder.append(chars, 0, read);
		}
		String string = builder.toString();
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(string, Object.class);
		String indented =
			mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
																				System.out.println("LocationsResourceWithRestAssuredTest.shouldGetRefreshToken() indented=" + indented);
		reader.close();
	}

	/**
	 * @throws Exception
	 * @see <a href="http://admin:password@localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=4398eaee-ae1a-4caf-8b08-2a2867ea9d2e&client_id=my-trusted-client></a>
	 */
	@Ignore @Test
	public void shouldGetAccessToken() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet("http:"
				+ "//admin"
				+ ":password"
				+ "@localhost"
				+ ":8080"
				+ "/oauth/token"
				+ "?grant_type=refresh_token"
				+ "&refresh_token=4398eaee-ae1a-4caf-8b08-2a2867ea9d2e"
				+ "&client_id=my-trusted-client");
//		BASE64Encoder encoder = new BASE64Encoder();
//		String encoded = encoder.encode("admin:password".getBytes());
//		request.setHeader("Authorization", "Basic " + encoded);
		HttpResponse response = client.execute(request);
//		assertEquals(200, response.getStatusLine().getStatusCode());
		Reader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		StringBuilder builder = new StringBuilder();
		char[] chars = new char[MAX_BUFFER_SIZE];
		int read;
		while ((read = reader.read(chars, 0, MAX_BUFFER_SIZE)) > 0) {
			builder.append(chars, 0, read);
		}
		String string = builder.toString();
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(string, Object.class);
		String indented =
			mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
																				System.out.println("LocationsResourceWithRestAssuredTest.shouldGetAccessToken() indented=" + indented);
		reader.close();
	}
}