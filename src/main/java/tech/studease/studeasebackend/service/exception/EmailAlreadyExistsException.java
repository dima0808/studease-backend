package tech.studease.studeasebackend.service.exception;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String credentials) {
    super(String.format("User with email '%s' not found", credentials));
  }
}
