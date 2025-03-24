package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findByTestId(UUID testId);

  List<Question> findByCollectionName(String collectionName);
}
