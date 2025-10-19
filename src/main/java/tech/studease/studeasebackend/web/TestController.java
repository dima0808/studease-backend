package tech.studease.studeasebackend.web;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.studease.studeasebackend.domain.Credentials;
import tech.studease.studeasebackend.dto.QuestionDto;
import tech.studease.studeasebackend.dto.ResponseEntryRequestDto;
import tech.studease.studeasebackend.dto.TestInfo;
import tech.studease.studeasebackend.dto.TestSessionDto;
import tech.studease.studeasebackend.repository.entity.Question;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.TestMapper;
import tech.studease.studeasebackend.service.mapper.TestSessionMapper;

@RestController
@RequestMapping("/api/v1/tests")
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;
  private final TestMapper testMapper;

  private final TestSessionService testSessionService;
  private final TestSessionMapper testSessionMapper;
  private final QuestionMapper questionMapper;

  @GetMapping("{testId}")
  public ResponseEntity<TestInfo> getTestById(@PathVariable UUID testId) {
    return ResponseEntity.ok(testMapper.toTestInfo(testService.findById(testId)));
  }

  @PostMapping("{testId}/start")
  public ResponseEntity<TestSessionDto> startTest(
      @PathVariable UUID testId, @RequestBody Credentials credentials) {
    return ResponseEntity.ok(
        testSessionMapper.toTestSessionDto(
            testSessionService.startTestSession(testId, credentials), false));
  }

  @PostMapping("{testId}/next-question")
  public ResponseEntity<QuestionDto> getNextQuestion(
      @PathVariable UUID testId, @RequestBody ResponseEntryRequestDto requestDto) {
    TestSession testSession =
        testSessionService.findByTestIdAndCredentials(testId, requestDto.getCredentials());

    Question question;
    if (requestDto.getAnswerContent() != null) {
      question = testSessionService.nextQuestion(testSession, requestDto.getAnswerContent());
    } else {
      question = testSessionService.nextQuestion(testSession, requestDto.getAnswerIds());
    }

    return ResponseEntity.ok(questionMapper.toQuestionDto(question, false));
  }

  @PostMapping("{testId}/finish")
  public ResponseEntity<TestSessionDto> finishTest(
      @PathVariable UUID testId, @RequestBody ResponseEntryRequestDto requestDto) {
    TestSession testSession =
        testSessionService.findByTestIdAndCredentials(testId, requestDto.getCredentials());

    if (requestDto.getAnswerContent() != null) {
      testSessionService.finishTestSession(testSession, requestDto.getAnswerContent());
    } else {
      testSessionService.finishTestSession(testSession, requestDto.getAnswerIds());
    }

    return ResponseEntity.ok(testSessionMapper.toTestSessionDto(testSession, true));
  }
}
