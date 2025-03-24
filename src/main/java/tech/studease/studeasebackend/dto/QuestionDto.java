package tech.studease.studeasebackend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import tech.studease.studeasebackend.validation.ValidAnswers;
import tech.studease.studeasebackend.validation.ValidQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ValidAnswers
public class QuestionDto {

  private Long id;

  @Size(min = 1, max = 250, message = "Question must be between 1 and 250 characters")
  @JsonPropertyDescription("The question to be answered")
  private String content;

  @NotNull(message = "Points is mandatory")
  @Min(value = 1, message = "Question points must be greater than 0")
  @JsonPropertyDescription("Points for the question")
  private Integer points;

  @ValidQuestionType
  @JsonPropertyDescription("Question type. One of: single_choice, multiple_choices, matching, essay")
  private String type;

  @Valid
  private List<AnswerDto> answers;
}
