package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.Optional;
import tech.studease.studeasebackend.repository.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

  List<Collection> findAllByAuthorEmail(String authorEmail);

  Optional<Collection> findByNameAndAuthorEmail(String name, String authorEmail);

  boolean existsByNameAndAuthorEmail(String name, String authorEmail);

  void deleteByName(String collectionName);
}
