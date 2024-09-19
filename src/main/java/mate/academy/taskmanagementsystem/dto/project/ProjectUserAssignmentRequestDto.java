package mate.academy.taskmanagementsystem.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProjectUserAssignmentRequestDto {
    @Positive
    @NotNull
    private Long userId;
}
