package tech.studease.studeasebackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import tech.studease.studeasebackend.common.QuestionType;

@RequiredArgsConstructor
public class QuestionTypeValidator implements ConstraintValidator<ValidQuestionType, String> {

  @Override
  public boolean isValid(String questionType, ConstraintValidatorContext context) {
    return Stream.of(QuestionType.values())
        .map(QuestionType::getDisplayName)
        .toList()
        .contains(questionType.toLowerCase());
  }
}
