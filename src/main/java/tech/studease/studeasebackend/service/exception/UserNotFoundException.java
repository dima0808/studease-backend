package tech.studease.studeasebackend.service.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String credentials) {
    super(String.format("User with email '%s' not found", credentials));
  }
}
