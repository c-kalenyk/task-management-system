package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTask;
import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTaskDto;
import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTaskRequestDto;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createAdditionalTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.TaskMapper;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.project.ProjectRepository;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.impl.TaskServiceImpl;
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
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Verify that correct TaskDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnCorrectProjectDto() {
        //Given
        Task task = createTestTask();
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        TaskDto expected = createTestTaskDto(task);

        when(projectRepository.findById(requestDto.getProjectId()))
                .thenReturn(Optional.of(task.getProject()));
        when(userRepository.findById(requestDto.getAssigneeId()))
                .thenReturn(Optional.of(task.getAssignee()));
        when(taskMapper.toModel(requestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(expected);
        //When
        TaskDto actual = taskService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(requestDto.getProjectId());
        verify(userRepository, times(1)).findById(requestDto.getAssigneeId());
        verify(taskMapper, times(1)).toModel(requestDto);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toDto(task);
        verifyNoMoreInteractions(projectRepository, userRepository, taskMapper, taskRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct TaskDto was returned when calling create() method
            without assignee""")
    public void create_WithValidRequestDtoWithoutUser_ShouldReturnCorrectProjectDto() {
        //Given
        Task task = createTestTask();
        task.setAssignee(null);
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        TaskDto expected = createTestTaskDto(task);

        when(projectRepository.findById(requestDto.getProjectId()))
                .thenReturn(Optional.of(task.getProject()));
        when(taskMapper.toModel(requestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(expected);
        //When
        TaskDto actual = taskService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(requestDto.getProjectId());
        verify(taskMapper, times(1)).toModel(requestDto);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toDto(task);
        verifyNoMoreInteractions(projectRepository, taskMapper, taskRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            with invalid project id""")
    public void create_WithInvalidProjectId_ShouldThrowException() {
        //Given
        TaskRequestDto requestDto = createTestTaskRequestDto(createTestTask());
        Long id = 2L;
        requestDto.setProjectId(id);

        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.create(requestDto)
        );
        //Then
        String expected = "Can't find project by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            with invalid user id""")
    public void create_WithInvalidUserId_ShouldThrowException() {
        //Given
        Task task = createTestTask();
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        Long userId = 2L;
        requestDto.setAssigneeId(userId);

        when(projectRepository.findById(task.getProject().getId()))
                .thenReturn(Optional.of(task.getProject()));
        when(userRepository.findById(requestDto.getAssigneeId())).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.create(requestDto)
        );
        //Then
        String expected = "Can't find user by id: " + userId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(requestDto.getProjectId());
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(projectRepository, userRepository);
    }

    @Test
    @DisplayName("Verify that correct Task list was returned when calling getAllTasks() method")
    public void getAllTasks_WithValidProjectId_ShouldReturnCorrectTaskSet() {
        //Given
        Task task = createTestTask();
        TaskDto taskDto = createTestTaskDto(task);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);
        List<TaskDto> expected = List.of(taskDto);
        Long projectId = task.getProject().getId();

        when(taskRepository.findAllByProjectId(projectId, pageable)).thenReturn(taskPage);
        when(taskMapper.toDtoList(taskPage.getContent())).thenReturn(expected);
        //When
        List<TaskDto> actual = taskService.getAllTasks(projectId, pageable);
        //Then
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findAllByProjectId(projectId, pageable);
        verify(taskMapper, times(1)).toDtoList(taskPage.getContent());
        verifyNoMoreInteractions(taskRepository, taskMapper);
    }

    @Test
    @DisplayName("Verify that correct TaskDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidTaskDto() {
        //Given
        Task task = createTestTask();
        TaskDto expected = createTestTaskDto(task);
        Long id = task.getId();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expected);
        //When
        TaskDto actual = taskService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findById(id);
        verify(taskMapper, times(1)).toDto(task);
        verifyNoMoreInteractions(taskRepository, taskMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.get(id)
        );
        //Then
        String expected = "Can't find task by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findById(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("Verify that correct TaskDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidTaskDto() {
        //Given
        Task task = createTestTask();
        task.setDescription("upd description");
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        TaskDto expected = createTestTaskDto(task);
        Long id = task.getId();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.toModel(requestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(expected);
        //When
        TaskDto actual = taskService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findById(id);
        verify(taskMapper, times(1)).toModel(requestDto);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toDto(task);
        verifyNoMoreInteractions(taskRepository, taskMapper);
    }

    @Test
    @DisplayName("""
            Verify that correct TaskDto was returned when calling update() method
            with changed assignee""")
    public void update_WithChangedUser_ShouldReturnValidTaskDto() {
        //Given
        Task existingTask = createTestTask();
        Task updatedTask = createTestTask();
        updatedTask.setAssignee(createAdditionalTestUser());
        TaskRequestDto requestDto = createTestTaskRequestDto(updatedTask);
        TaskDto expected = createTestTaskDto(updatedTask);
        Long id = existingTask.getId();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
        when(taskMapper.toModel(requestDto)).thenReturn(updatedTask);
        when(userRepository.findById(requestDto.getAssigneeId()))
                .thenReturn(Optional.of(updatedTask.getAssignee()));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(expected);
        //When
        TaskDto actual = taskService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findById(id);
        verify(taskMapper, times(1)).toModel(requestDto);
        verify(userRepository, times(1)).findById(requestDto.getAssigneeId());
        verify(taskRepository, times(1)).save(updatedTask);
        verify(taskMapper, times(1)).toDto(updatedTask);
        verifyNoMoreInteractions(taskRepository, taskMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling update() method
            with id of nonexistent project""")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        Task task = createTestTask();
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        Long id = 2L;
        requestDto.setProjectId(id);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toModel(requestDto)).thenReturn(task);
        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.update(task.getId(), requestDto)
        );
        //Then
        String expected = "Can't find project by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskMapper, times(1)).toModel(requestDto);
        verify(projectRepository, times(1)).findById(id);
        verifyNoMoreInteractions(taskRepository, taskMapper, projectRepository);
    }
}
