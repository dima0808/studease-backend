package tech.studease.studeasebackend.service.impl;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.QuestionRepository;
import tech.studease.studeasebackend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;

  @Override
  public List<Question> createAll(List<Question> questions) {
    return questionRepository.saveAll(questions);
  }

  @Override
  @Transactional
  public List<Question> findByTestId(UUID testId) {
    List<Question> questions = questionRepository.findByTestId(testId);
    questions.forEach(question -> Hibernate.initialize(question.getAnswers()));
    return questions;
  }

  @Override
  @Transactional
  public List<Question> findByCollectionName(String collectionName) {
    List<Question> questions = questionRepository.findByCollectionName(collectionName);
    questions.forEach(question -> Hibernate.initialize(question.getAnswers()));
    return questions;
  }
}
