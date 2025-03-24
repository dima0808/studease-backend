package tech.studease.studeasebackend.repository;

import java.util.Optional;
import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
}
