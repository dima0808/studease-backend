package tech.studease.studeasebackend.service;

import java.util.List;
import tech.studease.studeasebackend.repository.entity.Collection;

public interface CollectionService {

  List<Collection> findAll();

  Collection findByName(String collectionName);

  Collection create(Collection collection);

  void deleteByName(String collectionName);
}
