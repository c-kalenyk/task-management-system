package mate.academy.taskmanagementsystem.service.internal.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.CommentMapper;
import mate.academy.taskmanagementsystem.model.Comment;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.comment.CommentRepository;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public CommentDto create(Long userId, CreateCommentRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Task task = taskRepository.findById(requestDto.getTaskId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find task by id: " + requestDto.getTaskId()));
        Comment comment = commentMapper.toModel(requestDto);
        comment.setTask(task);
        comment.setUser(user);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAllComments(Long taskId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByTaskId(taskId, pageable);
        return commentMapper.toDtoList(commentPage.getContent());
    }
}
