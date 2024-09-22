package mate.academy.taskmanagementsystem.dto.project;

import java.time.LocalDate;

public record ProjectSearchParameters(
        String namePart,
        LocalDate endDate,
        String status
) {
}
