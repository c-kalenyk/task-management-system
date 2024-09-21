package mate.academy.taskmanagementsystem.repository.attachment;

import java.util.List;
import mate.academy.taskmanagementsystem.model.Attachment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @EntityGraph(attributePaths = {"task.project", "task.assignee", "task.labels"})
    List<Attachment> findAllByTaskId(Long taskId);
}
