package mate.academy.taskmanagementsystem.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.taskmanagementsystem.model.Project;

@Data
public class UpdateProjectStatusRequestDto {
    @NotNull
    private Project.Status status;
}
