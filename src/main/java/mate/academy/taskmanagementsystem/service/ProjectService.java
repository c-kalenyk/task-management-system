package mate.academy.taskmanagementsystem.service;

import java.util.Set;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectUserAssignmentRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectStatusRequestDto;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectDto create(Long userId, CreateProjectRequestDto requestDto);

    Set<ProjectDto> getAllProjects(Long userId, Pageable pageable);

    ProjectDto get(Long id);

    ProjectDto update(Long id, UpdateProjectRequestDto requestDto);

    ProjectDto updateStatus(Long id, UpdateProjectStatusRequestDto requestDto);

    ProjectDto addUser(Long id, ProjectUserAssignmentRequestDto requestDto);

    ProjectDto removeUser(Long id, ProjectUserAssignmentRequestDto requestDto);

    void delete(Long id);
}
