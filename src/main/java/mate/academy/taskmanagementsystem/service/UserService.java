package mate.academy.taskmanagementsystem.service;

import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.dto.user.UserRoleUpdateRequestDto;
import mate.academy.taskmanagementsystem.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserResponseDto get(Long id);

    UserResponseDto updateRole(Long id, UserRoleUpdateRequestDto requestDto);

    UserResponseDto update(Long id, UserRegistrationRequestDto requestDto);
}
