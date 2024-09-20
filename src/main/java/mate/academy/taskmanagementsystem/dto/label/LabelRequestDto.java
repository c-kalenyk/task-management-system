package mate.academy.taskmanagementsystem.dto.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabelRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
}
