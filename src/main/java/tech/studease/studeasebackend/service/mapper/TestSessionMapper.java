package tech.studease.studeasebackend.service.mapper;

import java.util.List;
import tech.studease.studeasebackend.dto.ResponseEntryDto;
import tech.studease.studeasebackend.dto.TestSessionDto;
import tech.studease.studeasebackend.dto.TestSessionListDto;
import tech.studease.studeasebackend.repository.entity.ResponseEntry;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.repository.projection.testsession.TestSessionWithoutResponsesProjection;
import tech.studease.studeasebackend.util.TestUtils;

public interface TestSessionMapper {

  default TestSessionDto toTestSessionDto(TestSession testSession, boolean includeResponses) {
    return toTestSessionDto(testSession, includeResponses, false);
  }

  default TestSessionDto toTestSessionDto(
      TestSession testSession, boolean includeResponses, boolean isAdmin) {
    return TestSessionDto.builder()
        .sessionId(testSession.getId().toString())
        .studentGroup(testSession.getStudentGroup())
        .studentName(testSession.getStudentName())
        .startedAt(testSession.getStartedAt())
        .finishedAt(testSession.getFinishedAt())
        .currentQuestionIndex(testSession.getCurrentQuestionIndex())
        .responses(
            includeResponses ? toResponseEntryDtoList(testSession.getResponses(), isAdmin) : null)
        .mark(
            (isAdmin && testSession.getFinishedAt() != null)
                ? testSession.getResponses().stream().mapToInt(TestUtils::calculateMark).sum()
                : null)
        .build();
  }

  default TestSessionListDto toTestSessionListDto(
      TestSession testSession, boolean includeResponses, boolean isAdmin) {
    return TestSessionListDto.builder()
        .sessions(List.of(toTestSessionDto(testSession, includeResponses, isAdmin)))
        .build();
  }

  default TestSessionListDto toTestSessionListDtoWithoutResponses(List<TestSessionWithoutResponsesProjection> testSessionsWithoutResponses) {
    return TestSessionListDto.builder()
            .sessions(toTestSessionDtoWithoutResponsesList(testSessionsWithoutResponses))
            .build();
  }

  default List<TestSessionDto> toTestSessionDtoWithoutResponsesList(List<TestSessionWithoutResponsesProjection> testSessionsWithoutResponses) {

    return testSessionsWithoutResponses.stream()
            .map(this::toTestSessionDtoWithoutResponses)
            .toList();
  }

  default TestSessionDto toTestSessionDtoWithoutResponses(TestSessionWithoutResponsesProjection projection) {
    return TestSessionDto.builder()
            .sessionId(String.valueOf(projection.getId()))
            .studentGroup(projection.getStudentGroup())
            .studentName(projection.getStudentName())
            .startedAt(projection.getStartedAt())
            .finishedAt(projection.getFinishedAt())
            .currentQuestionIndex(projection.getCurrentQuestionIndex())
            .build();
  }

  List<ResponseEntryDto> toResponseEntryDtoList(List<ResponseEntry> responses, boolean isAdmin);
}
