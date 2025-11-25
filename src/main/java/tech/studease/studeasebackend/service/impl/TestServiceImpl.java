package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.AuthUtils.getUserFromAuthentication;
import static tech.studease.studeasebackend.util.TestUtils.getFinishedSessions;
import static tech.studease.studeasebackend.util.TestUtils.getStartedSessions;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.studease.studeasebackend.dto.TestDeleteRequestDto;
import tech.studease.studeasebackend.repository.TestRepository;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.exception.ImmutableTestException;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  private final TestRepository testRepository;

  @Override
  public List<Test> findAll() {
    return testRepository.findByAuthorEmail(getUserFromAuthentication().getEmail());
  }

  @Override
  public Test findById(UUID testId) {
    Test test =
        testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
    if (!test.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
      throw new TestNotFoundException(testId);
    }
    return test;
  }

  @Override
  public Test findByIdForStudent(UUID testId) {
    return testRepository.getTestById(testId).orElseThrow(() -> new TestNotFoundException(testId));
  }

  @Override
  @Transactional
  public Test create(Test test) {
    test.setSessions(List.of());
    test.setAuthor(getUserFromAuthentication());
    return testRepository.save(test);
  }

  @Override
  public Test save(Test test) {
    return testRepository.save(test);
  }

  @Override
  public Test update(UUID testId, Test test) {
    Test testToUpdate = findById(testId);
    if (!test.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
      throw new TestNotFoundException(testId);
    }
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
    findById(testId);
    testRepository.deleteById(testId);
  }

  @Override
  public void deleteAllByIds(TestDeleteRequestDto request) {
    List<Test> tests = testRepository.findAllById(request.getTestIds());
    tests.forEach(
        t -> {
          if (!t.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
            throw new TestNotFoundException(t.getId());
          }
        });
    testRepository.deleteAll(tests);
  }
}
