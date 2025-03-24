package tech.studease.studeasebackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.studease.studeasebackend.dto.TestDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionSetValidator implements ConstraintValidator<ValidQuestionSet, TestDto> {

  @Override
  public boolean isValid(TestDto testDto, ConstraintValidatorContext context) {
    return (testDto.getQuestions() != null && !testDto.getQuestions().isEmpty()) ||
        (testDto.getSamples() != null && !testDto.getSamples().isEmpty());
  }
}
