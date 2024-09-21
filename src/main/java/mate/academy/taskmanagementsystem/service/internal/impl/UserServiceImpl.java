package mate.academy.taskmanagementsystem.service.internal.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.dto.user.UserRoleUpdateRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.exception.RegistrationException;
import mate.academy.taskmanagementsystem.exception.UsernameOrEmailExistsException;
import mate.academy.taskmanagementsystem.mapper.UserMapper;
import mate.academy.taskmanagementsystem.model.Role;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.role.RoleRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new RegistrationException("Can't register new user. "
                    + "User with username " + requestDto.getUsername() + " already exists");
        }
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register new user. "
                    + "User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.findByRoleName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto get(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id)));
    }

    @Override
    public UserResponseDto updateRole(Long id, UserRoleUpdateRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id));
        Role role = roleRepository.findByRoleName(requestDto.getRole());
        user.getRoles().add(role);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto update(Long id, UserRegistrationRequestDto requestDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id));
        if (!requestDto.getUsername().equals(existingUser.getUsername())
                && userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new UsernameOrEmailExistsException("Username already exists");
        }
        if (!requestDto.getEmail().equals(existingUser.getEmail())
                && userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UsernameOrEmailExistsException("Email already exists");
        }
        User updatedUser = userMapper.toModel(requestDto);
        updatedUser.setId(id);
        updatedUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        updatedUser.setRoles(existingUser.getRoles());
        return userMapper.toDto(userRepository.save(updatedUser));
    }
}
