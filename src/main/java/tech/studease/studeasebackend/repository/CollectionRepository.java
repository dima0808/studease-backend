package tech.studease.studeasebackend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.studease.studeasebackend.repository.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

  List<Collection> findAllByAuthorEmail(String authorEmail);

  Optional<Collection> findByIdAndAuthorEmail(Long id, String authorEmail);

  boolean existsByNameAndAuthorEmail(String name, String authorEmail);
}
