package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.studease.studeasebackend.repository.entity.TestSession;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Long> {

  Optional<TestSession> findTestSessionByStudentGroupAndStudentNameAndTestId(
      String studentGroup, String studentName, UUID testId);

  Optional<TestSession> findTestSessionBySessionId(String sessionId);

  boolean existsByStudentGroupAndStudentNameAndTestId(
      String studentGroup, String studentName, UUID testId);

  List<TestSession> findTestSessionsByTestId(UUID testId);
}
