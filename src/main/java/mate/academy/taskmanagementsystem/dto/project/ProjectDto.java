package mate.academy.taskmanagementsystem.dto.project;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Set<UserResponseDto> users;
}
