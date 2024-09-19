package mate.academy.taskmanagementsystem.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.taskmanagementsystem.annotation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword",
        message = "Passwords do not match")
public class UserRegistrationRequestDto {
    @NotBlank
    private String username;
    @Length(min = 6)
    private String password;
    @Length(min = 6)
    private String repeatPassword;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
