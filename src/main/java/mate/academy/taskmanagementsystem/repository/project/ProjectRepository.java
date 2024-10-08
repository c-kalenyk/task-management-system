package mate.academy.taskmanagementsystem.repository.project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.academy.taskmanagementsystem.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long>,
        JpaSpecificationExecutor<Project> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.users u "
            + "LEFT JOIN FETCH u.roles WHERE u.id = :userId")
    Page<Project> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "users.roles")
    Page<Project> findAll(Specification<Project> projectSpecification, Pageable pageable);

    @EntityGraph(attributePaths = "users.roles")
    List<Project> findAllByEndDate(LocalDate endDate);

    @EntityGraph(attributePaths = "users.roles")
    Optional<Project> findById(Long id);

    @Modifying
    @Query(value = "DELETE FROM projects_users WHERE project_id = :projectId AND user_id = :userId",
            nativeQuery = true)
    void removeUserFromProject(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
