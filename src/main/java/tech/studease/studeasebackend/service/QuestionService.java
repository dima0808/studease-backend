package tech.studease.studeasebackend.service;

import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Question;

public interface QuestionService {

  List<Question> createAll(List<Question> questions);

  List<Question> findByTestId(UUID testId);

  List<Question> findByCollectionId(Long collectionId);
}
