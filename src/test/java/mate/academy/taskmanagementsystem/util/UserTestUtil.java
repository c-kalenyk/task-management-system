package mate.academy.taskmanagementsystem.util;

import java.util.LinkedHashSet;
import java.util.Set;
import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.model.Role;
import mate.academy.taskmanagementsystem.model.User;

public class UserTestUtil {
    public static User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("bob");
        user.setPassword("bobster");
        user.setEmail("bob@example.test");
        user.setFirstName("Bob");
        user.setLastName("Admin");
        user.setRoles(Set.of(createAdminRole()));
        return user;
    }

    public static Role createAdminRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.ROLE_ADMIN);
        return role;
    }

    public static UserRegistrationRequestDto createTestUserRegistrationRequestDto(User user) {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setUsername(user.getUsername());
        requestDto.setPassword(user.getPassword());
        requestDto.setRepeatPassword(user.getPassword());
        requestDto.setEmail(user.getEmail());
        requestDto.setFirstName(user.getFirstName());
        requestDto.setLastName(user.getLastName());
        return requestDto;
    }

    public static UserResponseDto createTestUserResponseDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }

    private static Role createUserRole() {
        Role role = new Role();
        role.setId(2L);
        role.setRoleName(Role.RoleName.ROLE_USER);
        return role;
    }

    public static User createAdditionalTestUser() {
        User user = new User();
        user.setId(2L);
        user.setUsername("alice");
        user.setPassword("aliceter");
        user.setEmail("alice@example.test");
        user.setFirstName("Alice");
        user.setLastName("User");
        user.setRoles(Set.of(createUserRole()));
        return user;
    }

    public static Set<User> createTestUsersSet() {
        Set<User> users = new LinkedHashSet<>();
        users.add(createTestUser());
        users.add(createAdditionalTestUser());
        return users;
    }
}
