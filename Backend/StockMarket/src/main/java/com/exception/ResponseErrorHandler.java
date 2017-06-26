package com.exception;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * Custom exception, thrown in case of a response error in RestTemplates.
 */
public class ResponseErrorHandler extends DefaultResponseErrorHandler {

	//spring will take care restemplate post method exception
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
	}
}
