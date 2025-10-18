package tech.studease.studeasebackend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.studease.studeasebackend.validation.ValidAnswer;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ValidAnswer
public class AnswerDto {

  private Long id;

  @JsonPropertyDescription(
      "Is the answer correct. Only for SINGLE_CHOICE and MULTIPLE_CHOICES question types")
  private Boolean isCorrect;

  @Size(min = 1, max = 250, message = "Answer must be between 1 and 250 characters")
  @JsonPropertyDescription(
      "The answer to the question. Only for SINGLE_CHOICE, MULTIPLE_CHOICES and ESSAY question types")
  private String content;

  @Size(min = 1, max = 250, message = "Answer must be between 1 and 250 characters")
  @JsonPropertyDescription("The left option of the correct pair. Only for MATCHING question type")
  private String leftOption;

  @Size(min = 1, max = 250, message = "Answer must be between 1 and 250 characters")
  @JsonPropertyDescription("The right option of the correct pair. Only for MATCHING question type")
  private String rightOption;
}
