package tech.studease.studeasebackend.web;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.studease.studeasebackend.dto.TestInfo;
import tech.studease.studeasebackend.service.TestService;
import tech.studease.studeasebackend.service.mapper.TestMapper;

@RestController
@RequestMapping("/api/v1/tests")
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;
  private final TestMapper testMapper;

  @GetMapping("{testId}")
  public ResponseEntity<TestInfo> getTestById(@PathVariable UUID testId) {
    return ResponseEntity.ok(testMapper.toTestInfo(testService.findById(testId)));
  }
}
