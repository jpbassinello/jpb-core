/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.jpb.exception.FailedToSendEmailException;
import br.com.jpb.util.StringUtil;

import com.cribbstechnologies.clients.mandrill.exception.RequestFailedException;
import com.cribbstechnologies.clients.mandrill.model.MandrillHtmlMessage;
import com.cribbstechnologies.clients.mandrill.model.MandrillMessageRequest;
import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.request.MandrillMessagesRequest;
import com.cribbstechnologies.clients.mandrill.request.MandrillRESTRequest;
import com.cribbstechnologies.clients.mandrill.util.MandrillConfiguration;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

/**
 * 
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Named
@Singleton
public class MandrillMailService {

	private static MandrillRESTRequest request = new MandrillRESTRequest();
	private static MandrillConfiguration config = new MandrillConfiguration();
	private static MandrillMessagesRequest messagesRequest = new MandrillMessagesRequest();
	private static HttpClient client;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Properties props = new Properties();

	@PostConstruct
	public void PostConstruct() {
		try {
			props.load(MandrillMailService.class.getClassLoader()
					.getResourceAsStream("mandrill.properties"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		config.setApiKey(props.getProperty("apiKey"));
		config.setApiVersion("1.0");
		config.setBaseURL("https://mandrillapp.com/api");
		request.setConfig(config);
		request.setObjectMapper(mapper);
		messagesRequest.setRequest(request);
		client = HttpClientBuilder.create().build();
		request.setHttpClient(client);
	}

	public void sendMessage(String emailFrom, String nameFrom,
			String[] emailsTo, String[] namesTo, String subject, String htmlBody)
			throws FailedToSendEmailException {
		MandrillMessageRequest mmr = new MandrillMessageRequest();
		MandrillHtmlMessage message = new MandrillHtmlMessage();
		Map<String, String> headers = new HashMap<>();
		message.setFrom_email(emailFrom);
		message.setFrom_name(nameFrom);
		message.setHeaders(headers);
		message.setHtml(htmlBody);
		message.setSubject(subject);
		MandrillRecipient[] recipients = new MandrillRecipient[emailsTo.length];
		for (int i = 0; i < emailsTo.length; i++) {
			MandrillRecipient mandrillRecipient = new MandrillRecipient(
					namesTo[i], emailsTo[i]);
			recipients[i] = mandrillRecipient;
		}
		message.setTo(recipients);
		message.setTrack_clicks(true);
		message.setTrack_opens(true);
		mmr.setMessage(message);
		try {
			messagesRequest.mySendMessage(mmr);
		} catch (RequestFailedException e) {
			throw new FailedToSendEmailException("Failed to send e-mail", e);
		}
	}

	public String getMandrillPropertyValue(String propertyKey) {
		return props.getProperty(propertyKey);
	}

	public String getMailBodyHtmlByFileInClassLoader(String resourceFileName,
			String... args) {
		InputStreamReader isr = null;
		try {
			InputStream is = MandrillMailService.class.getClassLoader()
					.getResourceAsStream(
							props.getProperty("html.template.resources.path")
									+ resourceFileName);
			isr = new InputStreamReader(is, "UTF-8");
			String bodyUnchanged = CharStreams.toString(isr);
			int c = 0;
			String body = bodyUnchanged;
			for (String a : args) {
				String replace = "##" + c++ + "##";
				body = body.replaceAll(replace, StringUtil.escapeHTML(a));
			}
			return body;
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex);
		} finally {
			try {
				Closeables.close(isr, true);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
