package tech.studease.studeasebackend.service;

import tech.studease.studeasebackend.common.QuestionType;
import tech.studease.studeasebackend.dto.QuestionListDto;

public interface OpenAIService {

  QuestionListDto generateQuestions(
      String theme, QuestionType questionsType, int points, int questionCount);
}
