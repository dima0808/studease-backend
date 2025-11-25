package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.AuthUtils.getUserFromAuthentication;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.studease.studeasebackend.repository.SampleRepository;
import tech.studease.studeasebackend.repository.TestRepository;
import tech.studease.studeasebackend.repository.entity.Sample;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.service.SampleService;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

  private final SampleRepository sampleRepository;
  private final TestRepository testRepository;

  @Override
  public List<Sample> findByTestId(UUID testId) {
    Test test =
        testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
    if (!test.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
      throw new TestNotFoundException(testId);
    }

    return sampleRepository.findByTestId(testId);
  }
}
