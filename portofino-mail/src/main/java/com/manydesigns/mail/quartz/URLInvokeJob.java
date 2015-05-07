package com.manydesigns.mail.quartz;

import java.net.HttpURLConnection;
import java.net.URL;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class URLInvokeJob implements Job {
	public static final Logger logger = LoggerFactory.getLogger(URLInvokeJob.class);
	public static final String URL_KEY = "url";

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		String urlToInvoke = null;
		try {
			urlToInvoke = jobExecutionContext.getMergedJobDataMap().get(URL_KEY).toString();
			logger.debug("URL to invoke: " + urlToInvoke);
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlToInvoke).openConnection();
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);
			urlConnection.connect();
			int responseCode = urlConnection.getResponseCode();
			if (responseCode != 200) {
				logger.warn("Invocation of URL " + urlToInvoke + " returned response code " + responseCode);
			}
		} catch (Exception e) {
			logger.error("Failed to invoke URL: " + urlToInvoke, e);
			throw new JobExecutionException(e);
		}
	}
}