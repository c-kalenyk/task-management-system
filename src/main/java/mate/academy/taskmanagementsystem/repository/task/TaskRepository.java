package mate.academy.taskmanagementsystem.repository.task;

import java.util.Optional;
import mate.academy.taskmanagementsystem.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "assignee.roles", "labels"})
    Optional<Task> findById(Long id);
}
