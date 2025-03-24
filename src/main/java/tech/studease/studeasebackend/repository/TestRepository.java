package tech.studease.studeasebackend.repository;

import java.util.UUID;
import tech.studease.studeasebackend.repository.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {

}
