package kpi.ficting.kpitestplatform.repository;

import kpi.ficting.kpitestplatform.repository.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
