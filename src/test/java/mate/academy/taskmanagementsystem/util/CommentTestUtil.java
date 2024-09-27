package mate.academy.taskmanagementsystem.util;

import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementsystem.model.Comment;

public class CommentTestUtil {
    public static Comment createTestComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTask(createTestTask());
        comment.setUser(comment.getTask().getAssignee());
        comment.setText("text");
        comment.setTimestamp(LocalDateTime.of(2024, 9, 27, 13, 46, 34));
        return comment;
    }

    public static CreateCommentRequestDto createTestCreateCommentRequestDto(Comment comment) {
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setTaskId(comment.getTask().getId());
        requestDto.setText(comment.getText());
        return requestDto;
    }

    public static CommentDto createTestCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setTaskId(comment.getTask().getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setText(comment.getText());
        commentDto.setTimestamp(comment.getTimestamp());
        return commentDto;
    }

    public static List<CommentDto> fillExpectedCommentDtoList() {
        List<CommentDto> expected = new ArrayList<>();
        expected.add(new CommentDto().setId(1L).setTaskId(1L).setUserId(1L).setText("text")
                .setTimestamp(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        expected.add(new CommentDto().setId(2L).setTaskId(1L).setUserId(1L).setText("test")
                .setTimestamp(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        expected.add(new CommentDto().setId(3L).setTaskId(1L).setUserId(1L).setText("txst")
                .setTimestamp(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        return expected;
    }
}
