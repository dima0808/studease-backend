package tech.studease.studeasebackend.repository.projection.testsession;

import java.time.LocalDateTime;

public interface TestSessionWithoutResponsesProjection {
  Long getId();

  String getStudentGroup();

  String getStudentName();

  LocalDateTime getStartedAt();

  LocalDateTime getFinishedAt();

  Integer getCurrentQuestionIndex();

  Integer getMark();
}
