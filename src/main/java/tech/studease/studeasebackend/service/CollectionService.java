package tech.studease.studeasebackend.service;

import java.util.List;
import tech.studease.studeasebackend.repository.entity.Collection;

public interface CollectionService {

  List<Collection> findAll();

  Collection findById(Long collectionId);

  Collection create(Collection collection);

  void deleteById(Long collectionId);
}
