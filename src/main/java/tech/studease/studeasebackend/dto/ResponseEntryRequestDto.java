package tech.studease.studeasebackend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.studease.studeasebackend.domain.Credentials;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseEntryRequestDto {

  private Credentials credentials;

  private List<Long> answerIds;
  private String answerContent;
}
