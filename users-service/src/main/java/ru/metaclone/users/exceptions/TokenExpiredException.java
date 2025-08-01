package ru.metaclone.users.exceptions;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends BaseException {
    public static final String CODE = "TOKEN_EXPIRED";

    public TokenExpiredException(String message) {
        super(message);
    }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
