package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.repository.projection.testsession.TestSessionWithoutResponsesProjection;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Long> {

  Optional<TestSession> findTestSessionByStudentGroupAndStudentNameAndTestId(
      String studentGroup, String studentName, UUID testId);

  boolean existsByStudentGroupAndStudentNameAndTestId(
      String studentGroup, String studentName, UUID testId);

  @Query(
      """
    select
        ts.id as id,
        ts.studentGroup as studentGroup,
        ts.studentName as studentName,
        ts.startedAt as startedAt,
        ts.finishedAt as finishedAt,
        ts.currentQuestionIndex as currentQuestionIndex,
        ts.mark as mark
    from TestSession ts
    where ts.test.id = :testId
""")
  List<TestSessionWithoutResponsesProjection> findTestSessionsByTestId(UUID testId);

  @Query(
      """
    select
        ts.id as id,
        ts.studentGroup as studentGroup,
        ts.studentName as studentName,
        ts.startedAt as startedAt,
        ts.finishedAt as finishedAt,
        ts.currentQuestionIndex as currentQuestionIndex,
        ts.mark as mark
    from TestSession ts
    where ts.finishedAt is not null
      and ts.test.id = :testId
""")
  List<TestSessionWithoutResponsesProjection> findByTest_IdAndFinishedAtIsNotNull(UUID testId);
}
