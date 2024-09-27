package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUser;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUserRegistrationRequestDto;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUserResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.exception.RegistrationException;
import mate.academy.taskmanagementsystem.exception.UsernameOrEmailExistsException;
import mate.academy.taskmanagementsystem.mapper.UserMapper;
import mate.academy.taskmanagementsystem.model.Role;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.role.RoleRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Verify that correct UserResponseDto was returned when calling register() method")
    public void register_WithValidRequestDto_ShouldReturnValidUserResponseDto()
            throws RegistrationException {
        //Given
        User user = createTestUser();
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(user);
        final UserResponseDto expected = createTestUserResponseDto(user);
        final Role role = user.getRoles().iterator().next();
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName(Role.RoleName.ROLE_USER)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.register(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findByUsername(requestDto.getUsername());
        verify(userRepository, times(1)).findByEmail(requestDto.getEmail());
        verify(userMapper, times(1)).toModel(requestDto);
        verify(passwordEncoder, times(1)).encode(requestDto.getPassword());
        verify(roleRepository, times(1)).findByRoleName(Role.RoleName.ROLE_USER);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder, roleRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling register() method
            and user with such username already exists""")
    public void register_WithExistingUsername_ShouldThrowException() {
        //Given
        User existingUser = createTestUser();
        existingUser.setUsername("upd username");
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(existingUser);

        when(userRepository.findByUsername(requestDto.getUsername()))
                .thenReturn(Optional.of(existingUser));
        //When
        Exception exception = assertThrows(
                RegistrationException.class,
                () -> userService.register(requestDto)
        );
        //Then
        String expected = "Can't register new user. User with username "
                + requestDto.getUsername() + " already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findByUsername(requestDto.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling register() method
            and user with such email already exists""")
    public void register_WithExistingEmail_ShouldThrowException() {
        //Given
        User existingUser = createTestUser();
        existingUser.setEmail("upd email");
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(existingUser);

        when(userRepository.findByUsername(requestDto.getUsername()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(existingUser));
        //When
        Exception exception = assertThrows(
                RegistrationException.class,
                () -> userService.register(requestDto)
        );
        //Then
        String expected = "Can't register new user. User with this email already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findByUsername(requestDto.getUsername());
        verify(userRepository, times(1)).findByEmail(requestDto.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Verify that correct UserResponseDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDto(user);
        Long id = expected.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.get(id)
        );
        //Then
        String expected = "Can't find user by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Verify that correct UserResponseDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        user.setFirstName("updated");
        user.setPassword("encodedPassword");
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(user);
        UserResponseDto expected = createTestUserResponseDto(user);
        Long id = expected.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toModel(requestDto);
        verify(passwordEncoder, times(1)).encode(requestDto.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling update() method
            with username which already exists""")
    public void update_WithInvalidRequestDto_ShouldThrowException() {
        //Given
        User existingUser = createTestUser();
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(existingUser);
        requestDto.setUsername("upd username");
        Long id = existingUser.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(requestDto.getUsername()))
                .thenReturn(Optional.of(existingUser));
        //When
        Exception exception = assertThrows(
                UsernameOrEmailExistsException.class,
                () -> userService.update(id, requestDto)
        );
        //Then
        String expected = "Username already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByUsername(requestDto.getUsername());
        verifyNoMoreInteractions(userRepository);
    }
}
