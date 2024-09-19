package mate.academy.taskmanagementsystem.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.taskmanagementsystem.model.Role;

@Data
public class UserRoleUpdateRequestDto {
    @NotNull
    private Role.RoleName role;
}
