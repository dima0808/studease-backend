package tech.studease.studeasebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.studease.studeasebackend.repository.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {}
