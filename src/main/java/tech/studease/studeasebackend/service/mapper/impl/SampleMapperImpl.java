package tech.studease.studeasebackend.service.mapper.impl;

import java.util.List;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.repository.entity.Sample;
import tech.studease.studeasebackend.repository.entity.Test;
import tech.studease.studeasebackend.dto.SampleDto;
import tech.studease.studeasebackend.service.CollectionService;
import tech.studease.studeasebackend.service.exception.QuestionMergeConflictException;
import tech.studease.studeasebackend.service.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SampleMapperImpl implements SampleMapper {

  private final CollectionService collectionService;

  @Override
  public List<Sample> toSampleList(List<SampleDto> sampleDtos, Test test) {
    if (sampleDtos == null || sampleDtos.isEmpty()) {
      return List.of();
    }
    return sampleDtos.stream()
        .map((sampleDto) -> {
          Collection collection = collectionService.findByName(sampleDto.getCollectionName());
          long matchingQuestionsCount = collection.getQuestions().stream()
              .peek((question) -> {
                if (test.equals(question.getTest())) {
                  throw new QuestionMergeConflictException();
                }
              })
              .filter(question -> question.getPoints().equals(sampleDto.getPoints()))
              .count();
          if (sampleDto.getQuestionsCount() > matchingQuestionsCount) {
            throw new IllegalArgumentException(
                "Questions count in sample exceeds the number of matching questions in the collection");
          }
          return Sample.builder()
              .points(sampleDto.getPoints())
              .questionsCount(sampleDto.getQuestionsCount())
              .collection(collection)
              .test(test)
              .build();
        })
        .toList();
  }
}
