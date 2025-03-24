package tech.studease.studeasebackend.service.mapper;

import java.util.List;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.dto.CollectionDto;
import tech.studease.studeasebackend.dto.CollectionInfo;
import tech.studease.studeasebackend.dto.CollectionListInfo;

public interface CollectionMapper {

  default CollectionInfo toCollectionInfo(Collection collection) {
    return CollectionInfo.builder()
        .id(collection.getId())
        .name(collection.getName())
        .questionsCount(collection.getQuestions().size())
        .build();
  }

  default List<CollectionInfo> toCollectionInfo(List<Collection> collections) {
    return collections.stream()
        .map(this::toCollectionInfo)
        .toList();
  }

  default CollectionListInfo toCollectionListInfo(List<Collection> collections) {
    return CollectionListInfo.builder()
        .collections(toCollectionInfo(collections))
        .build();
  }

  Collection toCollection(CollectionDto collectionDto);
}
