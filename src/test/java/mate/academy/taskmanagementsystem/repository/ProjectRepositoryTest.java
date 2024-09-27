package mate.academy.taskmanagementsystem.repository;

import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProject;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.project.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(GlobalSetupExtension.class)
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Find projects by user id")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByUserId_GivenValidUserId_ShouldReturnProject() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Project> page = projectRepository.findAllByUserId(1L, pageable);
        Project actual = page.get().iterator().next();

        assertEquals(1L, actual.getId());
    }

    @Test
    @DisplayName("Find projects by max end date")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByEndDate_GivenValidDate_ShouldReturnProject() {
        List<Project> actual = projectRepository.findAllByEndDate(LocalDate.of(2024, 10, 26));

        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Unassign user from project")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void removeUserFromProject_GivenValidUserId_ShouldRemoveUser() {
        Project expected = createTestProject();
        expected.setUsers(Collections.emptySet());
        projectRepository.removeUserFromProject(1L, 1L);
        Project actual = projectRepository.findById(1L).orElseThrow();

        assertEquals(expected.getUsers().size(), actual.getUsers().size());
    }
}
