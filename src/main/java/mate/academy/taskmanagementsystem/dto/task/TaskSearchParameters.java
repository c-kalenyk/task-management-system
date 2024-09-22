package mate.academy.taskmanagementsystem.dto.task;

import java.time.LocalDate;

public record TaskSearchParameters(
        String namePart,
        String priority,
        String status,
        LocalDate dueDate
) {
}
