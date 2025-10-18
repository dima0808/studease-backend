package tech.studease.studeasebackend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.studease.studeasebackend.common.QuestionType;
import tech.studease.studeasebackend.dto.ResponseEntryDto;
import tech.studease.studeasebackend.repository.entity.Answer;
import tech.studease.studeasebackend.repository.entity.Essay;
import tech.studease.studeasebackend.repository.entity.ResponseEntry;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.TestSessionMapper;

@Component
@RequiredArgsConstructor
public class TestSessionMapperImpl implements TestSessionMapper {

  private final QuestionMapper questionMapper;

  @Override
  public List<ResponseEntryDto> toResponseEntryDtoList(
      List<ResponseEntry> responses, boolean isAdmin) {
    return responses.stream()
        .map(
            responseEntry ->
                ResponseEntryDto.builder()
                    .question(questionMapper.toQuestionDto(responseEntry.getQuestion(), isAdmin))
                    .answerIds(
                        responseEntry.getAnswers().stream()
                            .map(Answer::getId)
                            .collect(Collectors.toList()))
                    .answerContent(
                        responseEntry.getQuestion().getType() == QuestionType.ESSAY
                            ? ((Essay) responseEntry.getAnswers().get(0)).getContent()
                            : null)
                    .build())
        .collect(Collectors.toList());
  }
}
