package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.service.TaskService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Task managing", description = "Endpoints for managing tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create task", description = "Create a new task")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskDto createTask(@RequestBody @Valid TaskRequestDto requestDto) {
        return taskService.create(requestDto);
    }

    @Operation(summary = "Retrieve all tasks",
            description = "Retrieve all tasks for the project")
    @GetMapping("/project/{projectId}")
    public List<TaskDto> getAllTasks(@PathVariable Long projectId,
                                     @ParameterObject @PageableDefault Pageable pageable) {
        return taskService.getAllTasks(projectId, pageable);
    }

    @Operation(summary = "Get task", description = "Retrieve task details")
    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable Long id) {
        return taskService.get(id);
    }

    @Operation(summary = "Update task", description = "Update the task")
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id,
                              @RequestBody TaskRequestDto requestDto) {
        return taskService.update(id, requestDto);
    }

    @Operation(summary = "Delete task", description = "Delete the task")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
