package kpi.ficting.kpitestplatform.repository;

import java.util.Optional;
import kpi.ficting.kpitestplatform.repository.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(String name);
}
