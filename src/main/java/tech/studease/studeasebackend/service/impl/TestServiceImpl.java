package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.TestUtils.getFinishedSessions;
import static tech.studease.studeasebackend.util.TestUtils.getStartedSessions;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.UserRepository;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.repository.TestRepository;
import tech.studease.studeasebackend.repository.entity.User;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.exception.ImmutableTestException;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;
import tech.studease.studeasebackend.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final TestRepository testRepository;
  private final UserRepository userRepository;

  @Override
  public List<Test> findAll() {
    return getUserFromAuthContext().getTests();
  }

  @Override
  public Test findById(UUID testId) {
    return testRepository.findById(testId)
        .orElseThrow(() -> new TestNotFoundException(testId));
  }

  @Override
  @Transactional
  public Test create(Test test) {
    test.setSessions(List.of());
    test.setAuthor(getUserFromAuthContext());
    return testRepository.save(test);
  }

  @Override
  public Test save(Test test) {
    return testRepository.save(test);
  }

  @Override
  public Test update(UUID testId, Test test) {
    Test testToUpdate = findById(testId);
    if (getStartedSessions(test.getSessions()) != 0) {
      throw new ImmutableTestException(testId, "has started sessions");
    }
    if (getFinishedSessions(test.getSessions()) != 0) {
      throw new ImmutableTestException(testId, "has finished sessions");
    }
    testToUpdate.setName(test.getName());
    testToUpdate.setOpenDate(test.getOpenDate());
    testToUpdate.setDeadline(test.getDeadline());
    testToUpdate.setMinutesToComplete(test.getMinutesToComplete());

    test.getQuestions().forEach(question -> question.setTest(testToUpdate));
    testToUpdate.setQuestions(test.getQuestions());
    return testRepository.save(testToUpdate);
  }

  @Override
  public void deleteById(UUID testId) {
    testRepository.deleteById(testId);
  }

  private User getUserFromAuthContext() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException(userEmail));
  }
}
