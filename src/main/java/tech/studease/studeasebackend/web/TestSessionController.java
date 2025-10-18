package tech.studease.studeasebackend.web;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tech.studease.studeasebackend.dto.ResponseEntryDto;
import tech.studease.studeasebackend.dto.TestSessionDto;
import tech.studease.studeasebackend.dto.websocket.TestMessage;
import tech.studease.studeasebackend.dto.websocket.TestMessageType;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.TestSessionMapper;

@Controller
@RequiredArgsConstructor
public class TestSessionController {

  private final TestSessionService testSessionService;
  private final TestSessionMapper testSessionMapper;
  private final SimpMessagingTemplate messagingTemplate;
  private final QuestionMapper questionMapper;

  @MessageMapping("/tests/{testId}/start")
  @SendTo("/topic/tests/{testId}")
  public TestMessage startTestSession(
      @DestinationVariable UUID testId,
      @Payload TestSessionDto testSessionDto,
      @Header("credentials") String credentials,
      SimpMessageHeaderAccessor headerAccessor) {
    testSessionDto.setSessionId(headerAccessor.getSessionId());
    TestSession testSession =
        testSessionService.startTestSession(
            testId, testSessionMapper.toTestSession(testSessionDto));
    TestMessage testMessage =
        TestMessage.builder()
            .type(TestMessageType.START)
            .content("Test session started. " + credentials)
            .testSession(testSessionMapper.toTestSessionDto(testSession, false))
            .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/testSession", testMessage);
    return testMessage;
  }

  @MessageMapping("/tests/{testId}/healthcheck")
  public TestMessage healthcheck(
      @DestinationVariable UUID testId, @Header("credentials") String credentials) {
    TestSession testSession = testSessionService.findByTestIdAndCredentials(testId, credentials);
    TestMessage testMessage =
        TestMessage.builder()
            .type(TestMessageType.HEALTHCHECK)
            .content("Pong. Connection is stable. " + credentials)
            .testSession(testSessionMapper.toTestSessionDto(testSession, false))
            .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/testSession", testMessage);
    return testMessage;
  }

  @MessageMapping("/tests/{testId}/nextQuestion")
  public TestMessage nextQuestion(
      @DestinationVariable UUID testId, @Header("credentials") String credentials) {
    Question question = testSessionService.nextQuestion(testId, credentials);
    TestSession testSession = testSessionService.findByTestIdAndCredentials(testId, credentials);
    TestMessage testMessage =
        TestMessage.builder()
            .type(TestMessageType.NEXT_QUESTION)
            .content("Next question. " + credentials)
            .question(questionMapper.toQuestionDto(question, false))
            .testSession(testSessionMapper.toTestSessionDto(testSession, false))
            .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/testSession", testMessage);
    return testMessage;
  }

  @MessageMapping("/tests/{testId}/saveAnswer")
  public TestMessage saveAnswer(
      @DestinationVariable UUID testId,
      @Payload ResponseEntryDto responseEntryDto,
      @Header("credentials") String credentials) {
    TestSession testSession;
    if (responseEntryDto.getAnswerContent() == null) {
      testSession =
          testSessionService.saveAnswer(testId, credentials, responseEntryDto.getAnswerIds());
    } else {
      testSession =
          testSessionService.saveAnswer(testId, credentials, responseEntryDto.getAnswerContent());
    }
    TestMessage testMessage =
        TestMessage.builder()
            .type(TestMessageType.SAVE_ANSWER)
            .content("Save answer. " + credentials)
            .testSession(testSessionMapper.toTestSessionDto(testSession, false))
            .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/testSession", testMessage);
    return testMessage;
  }

  @MessageMapping("/tests/{testId}/finish")
  @SendTo({"/topic/tests", "/topic/tests/{testId}"})
  public TestMessage finishTestSession(
      @DestinationVariable UUID testId, @Header("credentials") String credentials) {
    TestSession testSession = testSessionService.finishTestSession(testId, credentials);
    TestMessage testMessage =
        TestMessage.builder()
            .type(TestMessageType.FINISH)
            .content("Test session finished. " + credentials)
            .testSession(testSessionMapper.toTestSessionDto(testSession, true))
            .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/testSession", testMessage);
    return testMessage;
  }
}
