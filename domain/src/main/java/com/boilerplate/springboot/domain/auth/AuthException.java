package com.boilerplate.springboot.domain.auth;

public class AuthException extends RuntimeException {

  public AuthException(String message) {
    super(message);
  }
}
