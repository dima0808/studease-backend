package tech.studease.studeasebackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Stream;
import tech.studease.studeasebackend.common.QuestionType;
import lombok.RequiredArgsConstructor;

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
