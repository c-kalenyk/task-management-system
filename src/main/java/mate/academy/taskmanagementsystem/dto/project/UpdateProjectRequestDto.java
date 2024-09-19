package mate.academy.taskmanagementsystem.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateProjectRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
