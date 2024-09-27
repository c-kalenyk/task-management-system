package mate.academy.taskmanagementsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
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
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Find tasks by project id")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql",
            "classpath:database/tasks/add-two-tasks.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql",
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByProjectId_GivenValidProjectId_ShouldReturnTaskPage() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Task> page = taskRepository.findAllByProjectId(1L, pageable);

        assertEquals(2, page.getContent().size());
    }

    @Test
    @DisplayName("Find tasks by dueDate")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql",
            "classpath:database/tasks/add-two-tasks.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql",
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByDueDate_GivenValidDate_ShouldReturnTaskList() {
        List<Task> actual = taskRepository.findAllByDueDate(LocalDate.of(2024, 10, 26));

        assertEquals(1, actual.size());
    }
}
