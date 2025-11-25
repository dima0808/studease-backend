package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.studease.studeasebackend.repository.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {

  @EntityGraph(attributePaths = {"sessions", "questions", "samples"})
  List<Test> findByAuthorEmail(String email);
}
