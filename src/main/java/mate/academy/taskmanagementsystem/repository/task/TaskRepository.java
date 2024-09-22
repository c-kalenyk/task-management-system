package mate.academy.taskmanagementsystem.repository.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.academy.taskmanagementsystem.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    List<Task> findAllByDueDate(LocalDate dueDate);

    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    Page<Task> findAll(Specification<Task> taskSpecification, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    Optional<Task> findById(Long id);
}
