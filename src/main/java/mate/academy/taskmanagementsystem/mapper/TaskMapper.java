package mate.academy.taskmanagementsystem.mapper;

import java.util.Collection;
import java.util.List;
import mate.academy.taskmanagementsystem.config.MapperConfig;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    TaskDto toDto(Task task);

    Task toModel(TaskRequestDto requestDto);

    List<TaskDto> toDtoList(Collection<Task> tasks);
}
