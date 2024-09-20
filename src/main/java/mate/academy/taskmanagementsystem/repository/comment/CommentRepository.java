package mate.academy.taskmanagementsystem.repository.comment;

import mate.academy.taskmanagementsystem.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {
            "user.roles",
            "task.assignee.roles",
            "task.project",
            "task.labels"
    })
    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);
}
