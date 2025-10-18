package tech.studease.studeasebackend.service.mapper;

import static tech.studease.studeasebackend.util.TestUtils.getFinishedSessions;
import static tech.studease.studeasebackend.util.TestUtils.getMaxScore;
import static tech.studease.studeasebackend.util.TestUtils.getQuestionsCount;
import static tech.studease.studeasebackend.util.TestUtils.getStartedSessions;

import java.util.List;
import tech.studease.studeasebackend.dto.TestDto;
import tech.studease.studeasebackend.dto.TestInfo;
import tech.studease.studeasebackend.dto.TestListInfo;
import tech.studease.studeasebackend.repository.entity.Test;

public interface TestMapper {

  default TestInfo toTestInfo(Test test, boolean isAdmin) {
    return TestInfo.builder()
        .id(test.getId().toString())
        .name(test.getName())
        .openDate(test.getOpenDate())
        .deadline(test.getDeadline())
        .minutesToComplete(test.getMinutesToComplete())
        .maxScore(getMaxScore(test.getQuestions(), test.getSamples()))
        .questionsCount(getQuestionsCount(test.getQuestions(), test.getSamples()))
        .startedSessions(isAdmin ? getStartedSessions(test.getSessions()) : null)
        .finishedSessions(isAdmin ? getFinishedSessions(test.getSessions()) : null)
        .build();
  }

  default TestInfo toTestInfo(Test test) {
    return toTestInfo(test, false);
  }

  default List<TestInfo> toTestInfo(List<Test> tests, boolean isAdmin) {
    return tests.stream().map((test) -> toTestInfo(test, isAdmin)).toList();
  }

  default TestListInfo toTestListInfo(List<Test> tests, boolean isAdmin) {
    return TestListInfo.builder().tests(toTestInfo(tests, isAdmin)).build();
  }

  Test toTest(TestDto testDto);
}
