package com.appNgeek.swaggerjwtdemo.exception;

public class AuthException extends Exception {

	private static final long serialVersionUID = -6561747157587815458L;

	public AuthException(String message) {
		super(message);
	}

	public AuthException(String message, Throwable ex) {
		super(message, ex);
	}

}
