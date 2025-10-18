package tech.studease.studeasebackend.service.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import tech.studease.studeasebackend.common.QuestionType;
import tech.studease.studeasebackend.dto.AnswerDto;
import tech.studease.studeasebackend.dto.QuestionDto;
import tech.studease.studeasebackend.dto.QuestionListDto;
import tech.studease.studeasebackend.repository.entity.Answer;
import tech.studease.studeasebackend.repository.entity.Choice;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.repository.entity.Essay;
import tech.studease.studeasebackend.repository.entity.MatchingPair;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.Test;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

  default QuestionDto toQuestionDto(Question question, boolean isAdmin) {
    return QuestionDto.builder()
        .id(question.getId())
        .content(question.getContent())
        .points(question.getPoints())
        .type(question.getType().getDisplayName())
        .answers(toAnswerDtoList(question.getAnswers(), isAdmin))
        .build();
  }

  default List<QuestionDto> toQuestionDto(List<Question> questions, boolean isAdmin) {
    return questions.stream().map((question) -> toQuestionDto(question, isAdmin)).toList();
  }

  default QuestionListDto toQuestionListDto(List<Question> questions, boolean isAdmin) {
    return QuestionListDto.builder().questions(toQuestionDto(questions, isAdmin)).build();
  }

  default List<AnswerDto> toAnswerDtoList(List<Answer> answers, boolean isAdmin) {
    return answers.stream()
        .map(
            (answer) -> {
              if (answer instanceof MatchingPair matchingPair) {
                return AnswerDto.builder()
                    .id(matchingPair.getId())
                    .leftOption(matchingPair.getLeftOption())
                    .rightOption(matchingPair.getRightOption())
                    .isCorrect(isAdmin ? matchingPair.getIsCorrect() : null)
                    .build();
              } else if (answer instanceof Choice choice) {
                return AnswerDto.builder()
                    .id(choice.getId())
                    .content(choice.getContent())
                    .isCorrect(isAdmin ? choice.getIsCorrect() : null)
                    .build();
              } else {
                Essay essay = (Essay) answer;
                return AnswerDto.builder().id(essay.getId()).content(essay.getContent()).build();
              }
            })
        .toList();
  }

  default List<Question> toQuestionList(List<QuestionDto> questionDtos, Test test) {
    if (questionDtos == null || questionDtos.isEmpty()) {
      return List.of();
    }
    return questionDtos.stream()
        .map(
            (questionDto) -> {
              Question question =
                  Question.builder()
                      .content(questionDto.getContent())
                      .points(questionDto.getPoints())
                      .type(QuestionType.valueOf(questionDto.getType().toUpperCase()))
                      .test(test)
                      .build();
              if (question.getType() != QuestionType.ESSAY) {
                question.setAnswers(toAnswerList(questionDto.getAnswers(), question));
              }
              return question;
            })
        .toList();
  }

  default List<Question> toQuestionList(List<QuestionDto> questionDtos, Collection collection) {
    return questionDtos.stream()
        .map(
            (questionDto) -> {
              Question question =
                  Question.builder()
                      .content(questionDto.getContent())
                      .points(questionDto.getPoints())
                      .type(QuestionType.valueOf(questionDto.getType().toUpperCase()))
                      .collection(collection)
                      .build();
              if (question.getType() != QuestionType.ESSAY) {
                question.setAnswers(toAnswerList(questionDto.getAnswers(), question));
              }
              return question;
            })
        .toList();
  }

  default List<Answer> toAnswerList(List<AnswerDto> answerDtos, Question question) {
    List<Answer> answers = new ArrayList<>();
    for (AnswerDto answerDto : answerDtos) {
      if (question.getType() == QuestionType.MATCHING) {
        answerDtos.forEach(
            option ->
                answers.add(
                    MatchingPair.builder()
                        .leftOption(answerDto.getLeftOption())
                        .rightOption(option.getRightOption())
                        .isCorrect(answerDto.getRightOption().equals(option.getRightOption()))
                        .question(question)
                        .build()));
      } else {
        answers.add(
            Choice.builder()
                .content(answerDto.getContent())
                .isCorrect(answerDto.getIsCorrect())
                .question(question)
                .build());
      }
    }
    return answers;
  }
}
