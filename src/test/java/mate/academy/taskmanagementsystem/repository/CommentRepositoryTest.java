package mate.academy.taskmanagementsystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.model.Comment;
import mate.academy.taskmanagementsystem.repository.comment.CommentRepository;
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
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Find comments by task id")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql",
            "classpath:database/tasks/add-task.sql",
            "classpath:database/comments/add-three-comments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/tasks/delete-tasks.sql",
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByTaskId_GivenValidTaskId_ShouldReturnComment() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Comment> page = commentRepository.findAllByTaskId(1L, pageable);

        assertEquals(3, page.getContent().size());
    }
}
