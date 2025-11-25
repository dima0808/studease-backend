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
    List<Test> tests = testRepository.findByAuthorEmail(getUserFromAuthentication().getEmail());
    return tests;
  }

  @Override
  public Test findById(UUID testId) {
    return testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
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

  @Override
  public void deleteAllByIds(TestDeleteRequestDto request) {
    List<Test> collections = testRepository.findAllById(request.getTestIds());
    testRepository.deleteAll(collections);
  }
}
