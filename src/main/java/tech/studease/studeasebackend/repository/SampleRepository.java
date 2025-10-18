package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.studease.studeasebackend.repository.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {

  List<Sample> findByTestId(UUID testId);
}
