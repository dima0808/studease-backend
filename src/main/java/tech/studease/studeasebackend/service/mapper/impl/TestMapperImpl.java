package tech.studease.studeasebackend.service.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.studease.studeasebackend.dto.TestDto;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import tech.studease.studeasebackend.service.mapper.SampleMapper;
import tech.studease.studeasebackend.service.mapper.TestMapper;

@Component
@RequiredArgsConstructor
public class TestMapperImpl implements TestMapper {

  private final QuestionMapper questionMapper;
  private final SampleMapper sampleMapper;

  @Override
  public Test toTest(TestDto testDto) {
    Test test =
        Test.builder()
            .name(testDto.getName())
            .openDate(testDto.getOpenDate())
            .deadline(testDto.getDeadline())
            .minutesToComplete(testDto.getMinutesToComplete())
            .build();
    test.setQuestions(questionMapper.toQuestionList(testDto.getQuestions(), test));
    test.setSamples(sampleMapper.toSampleList(testDto.getSamples(), test));
    return test;
  }
}
