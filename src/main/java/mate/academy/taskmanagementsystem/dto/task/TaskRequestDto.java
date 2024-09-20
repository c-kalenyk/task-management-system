package mate.academy.taskmanagementsystem.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import mate.academy.taskmanagementsystem.model.Label;
import mate.academy.taskmanagementsystem.model.Task;

@Data
public class TaskRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Task.Priority priority;
    @NotNull
    private Task.Status status;
    @NotNull
    private LocalDate dueDate;
    @Positive
    @NotNull
    private Long projectId;
    @Positive
    private Long assigneeId;
    private Set<Label> labels;
}
