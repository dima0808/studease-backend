package kpi.ficting.kpitestplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kpi.ficting.kpitestplatform.dto.QuestionDto;

public class AnswersValidator implements ConstraintValidator<ValidAnswers, QuestionDto> {

  @Override
  public boolean isValid(QuestionDto questionDto, ConstraintValidatorContext context) {
    if ("essay".equalsIgnoreCase(questionDto.getType())) {
      return true;
    }
    return questionDto.getAnswers() != null && questionDto.getAnswers().size() >= 2;
  }
}
