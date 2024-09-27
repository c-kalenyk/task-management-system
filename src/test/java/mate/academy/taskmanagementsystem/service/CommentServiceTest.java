package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestComment;
import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestCommentDto;
import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestCreateCommentRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
import mate.academy.taskmanagementsystem.service.internal.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Verify that correct CommentDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnCorrectCommentDto() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        CommentDto expected = createTestCommentDto(comment);
        User user = comment.getUser();
        Task task = comment.getTask();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentMapper.toModel(requestDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(expected);
        //When
        CommentDto actual = commentService.create(user.getId(), requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(user.getId());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(commentMapper, times(1)).toModel(requestDto);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toDto(comment);
        verifyNoMoreInteractions(userRepository, taskRepository, commentMapper, commentRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            with invalid user id""")
    public void create_WithInvalidUserId_ShouldThrowException() {
        //Given
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(createTestComment());
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.create(userId, requestDto)
        );
        //Then
        String expected = "Can't find user by id: " + userId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            with invalid task id""")
    public void create_WithInvalidTaskId_ShouldThrowException() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        User user = comment.getUser();
        Long taskId = 2L;
        requestDto.setTaskId(taskId);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.create(user.getId(), requestDto)
        );
        //Then
        String expected = "Can't find task by id: " + taskId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(user.getId());
        verify(taskRepository, times(1)).findById(taskId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct Comment list was returned when calling
            getAllComments() method""")
    public void getAllComments_WithValidTaskId_ShouldReturnCorrectCommentList() {
        //Given
        Comment comment = createTestComment();
        CommentDto commentDto = createTestCommentDto(comment);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Comment> commentPage = new PageImpl<>(List.of(comment), pageable, 1);
        List<CommentDto> expected = List.of(commentDto);
        Long taskId = commentDto.getTaskId();

        when(commentRepository.findAllByTaskId(taskId, pageable)).thenReturn(commentPage);
        when(commentMapper.toDtoList(commentPage.getContent())).thenReturn(expected);
        //When
        List<CommentDto> actual = commentService.getAllComments(taskId, pageable);
        //Then
        assertEquals(expected, actual);

        verify(commentRepository, times(1)).findAllByTaskId(taskId, pageable);
        verify(commentMapper, times(1)).toDtoList(commentPage.getContent());
        verifyNoMoreInteractions(commentRepository, commentMapper);
    }
}
