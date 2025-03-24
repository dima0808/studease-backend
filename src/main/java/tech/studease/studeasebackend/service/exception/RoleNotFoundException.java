package tech.studease.studeasebackend.service.exception;

public class RoleNotFoundException extends RuntimeException {

  public RoleNotFoundException(String role) {
    super(String.format("Role with name '%s' not found", role));
  }
}
