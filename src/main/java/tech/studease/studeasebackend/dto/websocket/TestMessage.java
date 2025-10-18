package tech.studease.studeasebackend.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tech.studease.studeasebackend.dto.QuestionDto;
import tech.studease.studeasebackend.dto.TestSessionDto;

@AllArgsConstructor
@Data
@Builder
public class TestMessage {

  private TestMessageType type;
  private String content;
  private TestSessionDto testSession;
  private QuestionDto question;
}
