package tech.studease.studeasebackend.service;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.domain.Credentials;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.TestSession;

public interface TestSessionService {

  List<TestSession> findByTestId(UUID testId, boolean finishedOnly);

  TestSession findByTestIdAndCredentials(UUID testId, Credentials credentials);

  Question startTestSession(UUID testId, Credentials credentials);

  Question getCurrentQuestion(UUID testId, Credentials credentials);

  Question nextQuestion(TestSession testSession, List<Long> answerIds);

  Question nextQuestion(TestSession testSession, String answerContent);

  TestSession finishTestSession(TestSession testSession, List<Long> answerIds);

  TestSession finishTestSession(TestSession testSession, String answerContent);

  TestSession forceEndTestSession(Long testSessionId);
}
