package tech.studease.studeasebackend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnswersValidator.class)
public @interface ValidAnswers {

  String message() default
      "Question must have at least two answers and at least one correct answer";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
