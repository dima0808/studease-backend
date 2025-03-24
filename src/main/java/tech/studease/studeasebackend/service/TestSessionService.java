package tech.studease.studeasebackend.service;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.TestSession;

public interface TestSessionService {

  TestSession save(TestSession testSession);

  TestSession findByTestIdAndCredentials(UUID testId, String credentials, boolean finishedOnly);

  TestSession findByTestIdAndCredentials(UUID testId, String credentials);

  TestSession findBySessionId(String sessionId);

  List<TestSession> findByTestId(UUID testId, boolean finishedOnly);

  TestSession startTestSession(UUID testId, TestSession testSession);

  Question nextQuestion(UUID testId, String credentials);

  TestSession saveAnswer(UUID testId, String credentials, List<Long> answerIds);

  TestSession saveAnswer(UUID testId, String credentials, String answerContent);

  TestSession finishTestSession(UUID testId, String credentials);
}
