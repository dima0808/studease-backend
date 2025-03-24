package tech.studease.studeasebackend.service;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Sample;

public interface SampleService {

  List<Sample> findByTestId(UUID testId);
}
