package tech.studease.studeasebackend.service;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.dto.TestDeleteRequestDto;
import tech.studease.studeasebackend.repository.entity.Test;

public interface TestService {

  List<Test> findAll();

  Test findById(UUID testId);

  Test create(Test test);

  Test save(Test test);

  Test update(UUID testId, Test test);

  void deleteById(UUID testId);

  void deleteAllByIds(TestDeleteRequestDto request);
}
