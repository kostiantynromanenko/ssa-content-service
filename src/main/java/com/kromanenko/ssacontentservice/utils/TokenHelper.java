package com.kromanenko.ssacontentservice.utils;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;

import com.kromanenko.ssacontentservice.exception.JwtClaimVerificationException;

public class TokenHelper {

  private static final String USER_ID_CLAIM = "user_id";

  public static String getUserId(Jwt jwt) throws JwtClaimVerificationException {
    var userId = jwt.getClaimAsString(USER_ID_CLAIM);

    return Optional.ofNullable(userId)
        .orElseThrow(() -> new JwtClaimVerificationException("Token: user ID is not provided in claims"));
  }
}
