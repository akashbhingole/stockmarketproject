package com.conf.filter;

import java.io.Serializable;

/**
 * This class represents a google token, this is used for validation of incoming
 * requests.
 */
@SuppressWarnings("serial")
public class TokenDetails implements Serializable {

	public static final String TOKEN_DETAILS_KEY = "tokenDetails";
	private String username;
	private int expiresInMs;
	private final Long expireDateInMil;
	private String token;

	public TokenDetails(String username, int expiresInMs, Long expireDateInMil, String token) {
		this.username = username;
		this.expiresInMs = expiresInMs;
		this.expireDateInMil = expireDateInMil;
		this.token = token;
	}

	public int getExpiresInMs() {
		return expiresInMs;
	}

	public void setExpiresInMs(int expiresInMs) {
		this.expiresInMs = expiresInMs;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public boolean isExpired() {
		return this.expireDateInMil < System.currentTimeMillis();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
