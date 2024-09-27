package mate.academy.taskmanagementsystem.repository;

import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(GlobalSetupExtension.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find user by username")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/users_roles/set-admin-role-for-bob.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_roles/remove-roles-from-users.sql",
            "classpath:database/users/delete-users.sql",
            "classpath:database/roles/delete-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByUsername_GivenValidUsername_ShouldReturnUser() {
        User expected = createTestUser();
        User actual = userRepository.findByUsername("bob").get();

        assertEquals(expected.getFirstName(), actual.getFirstName());
    }

    @Test
    @DisplayName("Find user by email")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/users_roles/set-admin-role-for-bob.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_roles/remove-roles-from-users.sql",
            "classpath:database/users/delete-users.sql",
            "classpath:database/roles/delete-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByEmail_GivenValidEmail_ShouldReturnUser() {
        User expected = createTestUser();
        User actual = userRepository.findByEmail("bob@example.test").get();

        assertEquals(expected.getFirstName(), actual.getFirstName());
    }
}
