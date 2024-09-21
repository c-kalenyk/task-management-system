package mate.academy.taskmanagementsystem.service.internal;

import java.util.List;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto create(TaskRequestDto requestDto);

    List<TaskDto> getAllTasks(Long projectId, Pageable pageable);

    TaskDto get(Long id);

    TaskDto update(Long id, TaskRequestDto requestDto);

    void delete(Long id);
}
