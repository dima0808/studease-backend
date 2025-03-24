package tech.studease.studeasebackend.service.exception;

public class QuestionMergeConflictException extends RuntimeException {

  public QuestionMergeConflictException() {
    super("Question merge conflict");
  }
}
