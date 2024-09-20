package mate.academy.taskmanagementsystem.service;

import java.util.List;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(Long userId, CreateCommentRequestDto requestDto);

    List<CommentDto> getAllComments(Long taskId, Pageable pageable);
}
