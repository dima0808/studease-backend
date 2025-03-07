package kpi.ficting.kpitestplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kpi.ficting.kpitestplatform.dto.AnswerDto;
import kpi.ficting.kpitestplatform.dto.QuestionDto;

public class AnswersValidator implements ConstraintValidator<ValidAnswers, QuestionDto> {

  @Override
  public boolean isValid(QuestionDto questionDto, ConstraintValidatorContext context) {
    if ("essay".equalsIgnoreCase(questionDto.getType())) {
      return true;
    }
    if (questionDto.getAnswers() != null && questionDto.getAnswers().size() >= 2) {
      if ("matching".equalsIgnoreCase(questionDto.getType())) {
        return true;
      } else {
        return questionDto.getAnswers().stream().anyMatch(AnswerDto::getIsCorrect);
      }
    }
    return false;
  }
}
