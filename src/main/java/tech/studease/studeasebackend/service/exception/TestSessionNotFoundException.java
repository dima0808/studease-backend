package tech.studease.studeasebackend.service.exception;

public class TestSessionNotFoundException extends RuntimeException {

  public TestSessionNotFoundException(String studentGroup, String studentName) {
    super("Test session with student " + studentGroup + " " + studentName + " not found");
  }

  public TestSessionNotFoundException(Long sessionId) {
    super("Test session with sessionId " + sessionId + " not found");
  }
}
