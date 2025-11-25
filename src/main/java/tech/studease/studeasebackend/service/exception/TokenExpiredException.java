package tech.studease.studeasebackend.service.exception;

public class TokenExpiredException extends RuntimeException {

  public TokenExpiredException() {
    super("Authentication token has expired");
  }
}
