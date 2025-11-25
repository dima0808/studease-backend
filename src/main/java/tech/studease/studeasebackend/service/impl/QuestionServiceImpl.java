package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.AuthUtils.getUserFromAuthentication;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.studease.studeasebackend.repository.CollectionRepository;
import tech.studease.studeasebackend.repository.QuestionRepository;
import tech.studease.studeasebackend.repository.TestRepository;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.service.QuestionService;
import tech.studease.studeasebackend.service.exception.CollectionNotFoundException;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final CollectionRepository collectionRepository;
  private final TestRepository testRepository;

  @Override
  public List<Question> createAll(List<Question> questions) {
    return questionRepository.saveAll(questions);
  }

  @Override
  @Transactional
  public List<Question> findByTestId(UUID testId) {
    Test test =
        testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
    if (!test.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
      throw new TestNotFoundException(testId);
    }
    List<Question> questions = questionRepository.findByTestId(testId);
    questions.forEach(question -> Hibernate.initialize(question.getAnswers()));
    return questions;
  }

  @Override
  @Transactional
  public List<Question> findByCollectionId(Long collectionId) {
    Collection collection =
        collectionRepository
            .findById(collectionId)
            .orElseThrow(() -> new CollectionNotFoundException(collectionId));
    if (!collection.getAuthor().getEmail().equals(getUserFromAuthentication().getEmail())) {
      throw new CollectionNotFoundException(collectionId);
    }

    List<Question> questions = questionRepository.findByCollectionId(collectionId);
    questions.forEach(question -> Hibernate.initialize(question.getAnswers()));
    return questions;
  }
}
