package mate.academy.taskmanagementsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.model.Attachment;
import mate.academy.taskmanagementsystem.repository.attachment.AttachmentRepository;
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
public class AttachmentRepositoryTest {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    @DisplayName("Find attachments by task id")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql",
            "classpath:database/tasks/add-task.sql",
            "classpath:database/attachments/add-three-attachments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/attachments/delete-attachments.sql",
            "classpath:database/tasks/delete-tasks.sql",
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByTaskId_GivenValidTaskId_ShouldReturnComment() {
        List<Attachment> expected = attachmentRepository.findAllByTaskId(1L);

        assertEquals(3, expected.size());
    }
}
