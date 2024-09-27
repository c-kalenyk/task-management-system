package mate.academy.taskmanagementsystem.dto.comment;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String text;
    private LocalDateTime timestamp;
}
