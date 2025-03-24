package tech.studease.studeasebackend.web;

import static tech.studease.studeasebackend.util.CsvGeneratorUtils.generateCsv;

import jakarta.validation.Valid;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.dto.QuestionListDto;
import tech.studease.studeasebackend.dto.SampleListDto;
import tech.studease.studeasebackend.dto.TestDto;
import tech.studease.studeasebackend.dto.TestInfo;
import tech.studease.studeasebackend.dto.TestListInfo;
import tech.studease.studeasebackend.dto.TestSessionListDto;
import tech.studease.studeasebackend.service.QuestionService;
import tech.studease.studeasebackend.service.SampleService;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.TestMapper;
import tech.studease.studeasebackend.service.mapper.TestSessionMapper;
import tech.studease.studeasebackend.service.mapper.impl.SampleMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/tests")
@RequiredArgsConstructor
public class TestManagementController {

  private final TestService testService;
  private final TestMapper testMapper;

  private final QuestionService questionService;
  private final QuestionMapper questionMapper;

  private final SampleService sampleService;
  private final SampleMapperImpl sampleMapper;

  private final TestSessionService testSessionService;
  private final TestSessionMapper testSessionMapper;

  @GetMapping
  public ResponseEntity<TestListInfo> getAllTests() {
    return ResponseEntity.ok(testMapper.toTestListInfo(testService.findAll(), true));
  }

  @GetMapping("{testId}")
  public ResponseEntity<TestInfo> getTestInfoById(@PathVariable UUID testId) {
    return ResponseEntity.ok(testMapper.toTestInfo(testService.findById(testId), true));
  }

  @GetMapping("{testId}/questions")
  public ResponseEntity<QuestionListDto> getQuestionsByTestId(@PathVariable UUID testId) {
    return ResponseEntity.ok(
        questionMapper.toQuestionListDto(questionService.findByTestId(testId), true));
  }

  @GetMapping("{testId}/samples")
  public ResponseEntity<SampleListDto> getSamplesByTestId(@PathVariable UUID testId) {
    return ResponseEntity.ok(sampleMapper.toSampleListDto(sampleService.findByTestId(testId)));
  }

  @GetMapping("{testId}/finishedSessions")
  public ResponseEntity<Object> getFinishedSessionsByTestId(@PathVariable UUID testId,
      @RequestParam(defaultValue = "") String credentials) {
    if (credentials.isEmpty()) {
      return ResponseEntity.ok(
          testSessionMapper.toTestSessionListDto(
              testSessionService.findByTestId(testId, true), false));
    } else {
      return ResponseEntity.ok(
          testSessionMapper.toTestSessionDto(testSessionService.findByTestIdAndCredentials(
              testId, credentials, true), true, true));
    }
  }

  @GetMapping("{testId}/finishedSessions/csv")
  public ResponseEntity<byte[]> getFinishedSessionsByTestIdToCsv(@PathVariable UUID testId) {
    Test test = testService.findById(testId);
    TestSessionListDto testSessionListDto = testSessionMapper.toTestSessionListDto(
        testSessionService.findByTestId(testId, true), false);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "sessions_" + test.getName() + ".csv");
    byte[] csvBytes = generateCsv(testSessionListDto).getBytes();
    return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<TestInfo> createTest(@RequestBody @Valid TestDto testDto) {
    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(testMapper.toTestInfo(testService.create(testMapper.toTest(testDto)), true));
  }

  @PutMapping("{testId}")
  @Deprecated
  public ResponseEntity<TestInfo> updateTest(@PathVariable UUID testId,
      @RequestBody @Valid TestDto testDto) {
    return ResponseEntity.ok(
        testMapper.toTestInfo(testService.update(testId, testMapper.toTest(testDto)), true));
  }

  @DeleteMapping("{testId}")
  public ResponseEntity<Void> deleteTest(@PathVariable UUID testId) {
    testService.deleteById(testId);
    return ResponseEntity.noContent().build();
  }
}
