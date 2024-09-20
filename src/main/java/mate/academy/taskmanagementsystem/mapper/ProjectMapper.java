package mate.academy.taskmanagementsystem.mapper;

import java.util.Collection;
import java.util.Set;
import mate.academy.taskmanagementsystem.config.MapperConfig;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.model.Project;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface ProjectMapper {
    ProjectDto toDto(Project project);

    Project toModel(CreateProjectRequestDto createProjectRequestDto);

    Set<ProjectDto> toDtoSet(Collection<Project> projects);

    default Project updateProject(Project existingProject, UpdateProjectRequestDto requestDto) {
        existingProject.setName(requestDto.getName());
        existingProject.setDescription(requestDto.getDescription());
        existingProject.setStartDate(requestDto.getStartDate());
        existingProject.setEndDate(requestDto.getEndDate());
        return existingProject;
    }
}
