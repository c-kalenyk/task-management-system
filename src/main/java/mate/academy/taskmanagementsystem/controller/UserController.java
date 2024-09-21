package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.dto.user.UserRoleUpdateRequestDto;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.service.internal.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User managing", description = "Endpoints for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user info", description = "Get current user's profile info")
    @GetMapping("/me")
    public UserResponseDto getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.get(user.getId());
    }

    @Operation(summary = "Update user info", description = "Update current user's profile info")
    @PutMapping("/me")
    public UserResponseDto updateUserInfo(
            Authentication authentication,
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.update(user.getId(), requestDto);
    }

    @Operation(summary = "Update user role", description = "Update user's role")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public UserResponseDto updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid UserRoleUpdateRequestDto requestDto
    ) {
        return userService.updateRole(id, requestDto);
    }
}
