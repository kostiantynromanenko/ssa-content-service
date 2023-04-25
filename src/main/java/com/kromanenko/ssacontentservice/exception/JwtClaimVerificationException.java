package com.kromanenko.ssacontentservice.exception;

public class JwtClaimVerificationException extends RuntimeException {

  public JwtClaimVerificationException(String message) {
    super(message);
  }
}
