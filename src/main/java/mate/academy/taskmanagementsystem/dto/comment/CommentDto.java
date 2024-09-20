package mate.academy.taskmanagementsystem.dto.comment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String text;
    private LocalDateTime timestamp;
}
