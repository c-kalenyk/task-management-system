package mate.academy.taskmanagementsystem.dto.task;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import mate.academy.taskmanagementsystem.model.Label;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private Long projectId;
    private Long assigneeId;
    private Set<Label> labels;
}
