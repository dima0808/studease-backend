package tech.studease.studeasebackend.service.impl;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Sample;
import tech.studease.studeasebackend.repository.SampleRepository;
import tech.studease.studeasebackend.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

  private final SampleRepository sampleRepository;

  @Override
  public List<Sample> findByTestId(UUID testId) {
    return sampleRepository.findByTestId(testId);
  }
}
