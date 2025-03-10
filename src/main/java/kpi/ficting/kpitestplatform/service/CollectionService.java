package kpi.ficting.kpitestplatform.service;

import java.util.List;
import kpi.ficting.kpitestplatform.repository.entity.Collection;

public interface CollectionService {

  List<Collection> findAll();

  Collection findByName(String collectionName);

  Collection create(Collection collection);

  void deleteByName(String collectionName);
}
