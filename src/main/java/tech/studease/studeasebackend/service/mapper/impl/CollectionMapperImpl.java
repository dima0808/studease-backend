package tech.studease.studeasebackend.service.mapper.impl;

import java.util.List;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.dto.CollectionDto;
import tech.studease.studeasebackend.service.mapper.CollectionMapper;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionMapperImpl implements CollectionMapper {

  private final QuestionMapper questionMapper;

  public Collection toCollection(CollectionDto collectionDto) {
    Collection collection = Collection.builder()
        .name(collectionDto.getName())
        .build();
    collection.setQuestions(
        collectionDto.getQuestions() == null || collectionDto.getQuestions().isEmpty() ? List.of()
            : questionMapper.toQuestionList(collectionDto.getQuestions(), collection));
    return collection;
  }
}
