package tech.studease.studeasebackend.web;

import static tech.studease.studeasebackend.util.CsvGeneratorUtils.generateCsv;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.studease.studeasebackend.domain.Credentials;
import tech.studease.studeasebackend.dto.QuestionListDto;
import tech.studease.studeasebackend.dto.SampleListDto;
import tech.studease.studeasebackend.dto.TestDeleteRequestDto;
import tech.studease.studeasebackend.dto.TestDto;
import tech.studease.studeasebackend.dto.TestInfo;
import tech.studease.studeasebackend.dto.TestListInfo;
import tech.studease.studeasebackend.dto.TestSessionListDto;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.service.QuestionService;
import tech.studease.studeasebackend.service.SampleService;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.TestMapper;
import tech.studease.studeasebackend.service.mapper.TestSessionMapper;
import tech.studease.studeasebackend.service.mapper.impl.SampleMapperImpl;

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
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TestListInfo> getAllTests() {
    return ResponseEntity.ok(testMapper.toTestListInfo(testService.findAll(), true));
  }

  @GetMapping("{testId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TestInfo> getTestInfoById(@PathVariable UUID testId) {
    return ResponseEntity.ok(testMapper.toTestInfo(testService.findById(testId), true));
  }

  @GetMapping("{testId}/questions")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<QuestionListDto> getQuestionsByTestId(@PathVariable UUID testId) {
    return ResponseEntity.ok(
        questionMapper.toQuestionListDto(questionService.findByTestId(testId), true));
  }

  @GetMapping("{testId}/samples")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<SampleListDto> getSamplesByTestId(@PathVariable UUID testId) {
    return ResponseEntity.ok(sampleMapper.toSampleListDto(sampleService.findByTestId(testId)));
  }

  @GetMapping("{testId}/finishedSessions")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TestSessionListDto> getFinishedSessionsByTestId(
      @PathVariable UUID testId,
      @RequestParam(required = false) String studentName,
      @RequestParam(required = false) String studentGroup) {
    Credentials credentials =
        new Credentials(
            studentGroup == null ? "" : studentGroup, studentName == null ? "" : studentName);

    if (credentials.studentName().isBlank() && credentials.studentGroup().isBlank()) {
      return ResponseEntity.ok(
          testSessionMapper.toTestSessionListDtoWithoutResponses(
              testSessionService.findByTestId(testId, true)));
    } else {
      return ResponseEntity.ok(
          testSessionMapper.toTestSessionListDto(
              testSessionService.findByTestIdAndCredentials(testId, credentials), true, true));
    }
  }

  @GetMapping("{testId}/finishedSessions/csv")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<byte[]> getFinishedSessionsByTestIdToCsv(@PathVariable UUID testId) {
    Test test = testService.findById(testId);
    TestSessionListDto testSessionListDto =
        testSessionMapper.toTestSessionListDtoWithoutResponses(
            testSessionService.findByTestId(testId, true));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "sessions_" + test.getName() + ".csv");
    byte[] csvBytes = generateCsv(testSessionListDto).getBytes();
    return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TestInfo> createTest(@RequestBody @Valid TestDto testDto) {
    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(testMapper.toTestInfo(testService.create(testMapper.toTest(testDto)), true));
  }

  @PutMapping("{testId}")
  @Deprecated
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TestInfo> updateTest(
      @PathVariable UUID testId, @RequestBody @Valid TestDto testDto) {
    return ResponseEntity.ok(
        testMapper.toTestInfo(testService.update(testId, testMapper.toTest(testDto)), true));
  }

  @DeleteMapping("{testId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteTest(@PathVariable UUID testId) {
    testService.deleteById(testId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteCollections(@RequestBody @Valid TestDeleteRequestDto request) {
    testService.deleteAllByIds(request);
    return ResponseEntity.noContent().build();
  }
}
