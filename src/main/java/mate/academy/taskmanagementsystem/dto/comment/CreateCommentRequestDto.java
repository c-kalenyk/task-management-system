package mate.academy.taskmanagementsystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCommentRequestDto {
    @Positive
    @NotNull
    private Long taskId;
    @NotBlank
    private String text;
}
