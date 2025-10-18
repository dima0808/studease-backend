package tech.studease.studeasebackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
  SINGLE_CHOICE("single_choice"),
  MULTIPLE_CHOICES("multiple_choices"),
  MATCHING("matching"),
  ESSAY("essay");

  private final String displayName;
}
