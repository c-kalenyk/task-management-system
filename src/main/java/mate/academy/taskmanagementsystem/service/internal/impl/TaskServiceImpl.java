package mate.academy.taskmanagementsystem.service.internal.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.TaskMapper;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.project.ProjectRepository;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskDto create(TaskRequestDto requestDto) {
        Project project = projectRepository.findById(requestDto.getProjectId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: "
                        + requestDto.getProjectId()));
        User user = null;
        if (requestDto.getAssigneeId() != null) {
            user = userRepository.findById(requestDto.getAssigneeId()).orElseThrow(() ->
                    new EntityNotFoundException("Can't find user by id: "
                            + requestDto.getAssigneeId()));
        }
        Task task = taskMapper.toModel(requestDto);
        task.setProject(project);
        task.setAssignee(user);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> getAllTasks(Long projectId, Pageable pageable) {
        Page<Task> tasksPage = taskRepository.findAllByProjectId(projectId, pageable);
        return taskMapper.toDtoList(tasksPage.getContent());
    }

    @Override
    public TaskDto get(Long id) {
        return taskMapper.toDto(taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find task by id: " + id)));
    }

    @Override
    public TaskDto update(Long id, TaskRequestDto requestDto) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find task by id: " + id));
        Task updatedTask = taskMapper.toModel(requestDto);
        updatedTask.setId(id);
        if (!existingTask.getAssignee().getId().equals(requestDto.getAssigneeId())) {
            User newAssignee = userRepository.findById(requestDto.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find user by id: "
                            + requestDto.getAssigneeId()));
            updatedTask.setAssignee(newAssignee);
        } else {
            updatedTask.setAssignee(existingTask.getAssignee());
        }
        if (!existingTask.getProject().getId().equals(requestDto.getProjectId())) {
            Project newProject = projectRepository.findById(requestDto.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find project by id: "
                            + requestDto.getProjectId()));
            updatedTask.setProject(newProject);
        } else {
            updatedTask.setProject(existingTask.getProject());
        }
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
