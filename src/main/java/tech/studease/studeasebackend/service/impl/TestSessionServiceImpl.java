package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.event.GlobalTestSessionScheduler.addTimer;
import static tech.studease.studeasebackend.event.GlobalTestSessionScheduler.removeTimer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.studease.studeasebackend.common.QuestionType;
import tech.studease.studeasebackend.domain.Credentials;
import tech.studease.studeasebackend.repository.AnswerRepository;
import tech.studease.studeasebackend.repository.TestRepository;
import tech.studease.studeasebackend.repository.TestSessionRepository;
import tech.studease.studeasebackend.repository.entity.Answer;
import tech.studease.studeasebackend.repository.entity.Essay;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.ResponseEntry;
import tech.studease.studeasebackend.repository.entity.Sample;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;
import tech.studease.studeasebackend.service.exception.TestSessionAlreadyExistsException;
import tech.studease.studeasebackend.service.exception.TestSessionNotFoundException;

@Service
@RequiredArgsConstructor
public class TestSessionServiceImpl implements TestSessionService {

  private final TestSessionRepository testSessionRepository;
  private final TestRepository testRepository;
  private final AnswerRepository answerRepository;

  @Override
  @Transactional
  public List<TestSession> findByTestId(UUID testId, boolean finishedOnly) {
    List<TestSession> sessions = testSessionRepository.findTestSessionsByTestId(testId);
    if (finishedOnly) {
      sessions =
          sessions.stream().filter(s -> s.getFinishedAt() != null).collect(Collectors.toList());
    }
    sessions.forEach(
        s -> {
          Hibernate.initialize(s.getResponses());
          s.getResponses().forEach(r -> Hibernate.initialize(r.getQuestion().getAnswers()));
        });
    return sessions;
  }

  @Override
  public TestSession findByTestIdAndCredentials(UUID testId, Credentials credentials) {
    return testSessionRepository
        .findTestSessionByStudentGroupAndStudentNameAndTestId(
            credentials.studentGroup(), credentials.studentName(), testId)
        .orElseThrow(
            () ->
                new TestSessionNotFoundException(
                    credentials.studentGroup(), credentials.studentName()));
  }

  @Override
  public TestSession startTestSession(UUID testId, Credentials credentials) {
    String studentGroup = credentials.studentGroup();
    String studentName = credentials.studentName();
    if (testSessionRepository.existsByStudentGroupAndStudentNameAndTestId(
        studentGroup, studentName, testId)) {
      throw new TestSessionAlreadyExistsException(studentGroup, studentName);
    }

    Test test =
        testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));

    if (test.getOpenDate().isAfter(LocalDateTime.now())) {
      throw new IllegalStateException("Test is not open yet");
    }
    if (test.getDeadline().isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("Test is closed");
    }

    TestSession testSession =
        TestSession.builder()
            .studentGroup(studentGroup)
            .studentName(studentName)
            .startedAt(LocalDateTime.now())
            .currentQuestionIndex(0)
            .test(test)
            .build();

    List<ResponseEntry> responses = new ArrayList<>();
    addTestQuestions(responses, test.getQuestions(), testSession);
    addSampleQuestions(responses, test.getSamples(), testSession);
    Collections.shuffle(responses);
    testSession.setResponses(responses);

    testSessionRepository.save(testSession);

    addTimer(testSession.getId(), test.getMinutesToComplete() * 60);

    return testSession;
  }

  @Override
  public Question nextQuestion(TestSession testSession, List<Long> answerIds) {
    saveAnswers(testSession, answerIds);
    testSession.setCurrentQuestionIndex(testSession.getCurrentQuestionIndex() + 1);

    testSessionRepository.save(testSession);

    return nextResponseEntry(testSession).getQuestion();
  }

  @Override
  public Question nextQuestion(TestSession testSession, String answerContent) {
    saveAnswers(testSession, answerContent);
    testSession.setCurrentQuestionIndex(testSession.getCurrentQuestionIndex() + 1);

    testSessionRepository.save(testSession);

    return nextResponseEntry(testSession).getQuestion();
  }

  @Override
  public TestSession finishTestSession(TestSession testSession, List<Long> answerIds) {
    saveAnswers(testSession, answerIds);
    testSession.setFinishedAt(LocalDateTime.now());

    removeTimer(testSession.getId());

    return testSessionRepository.save(testSession);
  }

  @Override
  public TestSession finishTestSession(TestSession testSession, String answerContent) {
    saveAnswers(testSession, answerContent);
    testSession.setFinishedAt(LocalDateTime.now());

    removeTimer(testSession.getId());

    return testSessionRepository.save(testSession);
  }

  @Override
  public TestSession forceEndTestSession(Long testSessionId) {
    TestSession testSession =
        testSessionRepository
            .findById(testSessionId)
            .orElseThrow(() -> new TestSessionNotFoundException(testSessionId));

    testSession.setFinishedAt(LocalDateTime.now());

    removeTimer(testSessionId);

    return testSessionRepository.save(testSession);
  }

  private void saveAnswers(TestSession testSession, List<Long> answerIds) {
    ResponseEntry responseEntry = nextResponseEntry(testSession);
    if (responseEntry.getQuestion().getType() == QuestionType.ESSAY) {
      throw new IllegalArgumentException("Answer must be a text");
    }

    List<Answer> answers = new ArrayList<>(responseEntry.getQuestion().getAnswers());
    answers =
        answers.stream().filter(a -> answerIds.contains(a.getId())).collect(Collectors.toList());
    if (answers.isEmpty()) {
      throw new IllegalArgumentException("Answers must not be empty");
    }

    if (responseEntry.getQuestion().getType() == QuestionType.SINGLE_CHOICE && answers.size() > 1) {
      throw new IllegalArgumentException("Only one answer is allowed for SINGLE_CHOICE questions");
    }

    responseEntry.setAnswers(answers);
  }

  private void saveAnswers(TestSession testSession, String answerContent) {
    ResponseEntry responseEntry = nextResponseEntry(testSession);
    if (responseEntry.getQuestion().getType() != QuestionType.ESSAY) {
      throw new IllegalArgumentException("Answer must not be a text");
    }

    List<Answer> answers = new ArrayList<>();
    Answer essayAnswer =
        Essay.builder()
            .isCorrect(true)
            .content(answerContent)
            .question(responseEntry.getQuestion())
            .build();
    answerRepository.save(essayAnswer);
    answers.add(essayAnswer);
    responseEntry.setAnswers(answers);
  }

  private void addTestQuestions(
      List<ResponseEntry> responses, List<Question> testQuestions, TestSession testSession) {
    for (Question question : testQuestions) {
      responses.add(ResponseEntry.builder().question(question).testSession(testSession).build());
    }
  }

  private void addSampleQuestions(
      List<ResponseEntry> responses, List<Sample> samples, TestSession testSession) {
    Random random = new Random();
    for (Sample sample : samples) {
      List<Question> sampleQuestions = sample.getCollection().getQuestions();
      List<Question> selectedQuestions =
          sampleQuestions.stream()
              .filter(q -> q.getPoints().equals(sample.getPoints()))
              .collect(Collectors.toList());
      for (int i = 0; i < sample.getQuestionsCount(); i++) {
        Question question = selectedQuestions.remove(random.nextInt(selectedQuestions.size()));
        responses.add(ResponseEntry.builder().question(question).testSession(testSession).build());
      }
    }
  }

  private ResponseEntry nextResponseEntry(TestSession testSession) {
    return testSession.getResponses().stream()
        .filter(r -> r.getAnswers().isEmpty())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No more questions"));
  }
}
