package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectUserAssignmentRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectStatusRequestDto;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.service.internal.ProjectService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Project managing", description = "Endpoints for managing projects")
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Create project", description = "Create a new project")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProjectDto createProject(Authentication authentication,
                                    @RequestBody @Valid CreateProjectRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return projectService.create(user.getId(), requestDto);
    }

    @Operation(summary = "Retrieve user's projects",
            description = "Retrieve all projects of current user")
    @GetMapping
    public Set<ProjectDto> getAllProjects(Authentication authentication,
                                          @ParameterObject @PageableDefault Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return projectService.getAllProjects(user.getId(), pageable);
    }

    @Operation(summary = "Get project", description = "Retrieve project details")
    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable Long id) {
        return projectService.get(id);
    }

    @Operation(summary = "Update project", description = "Update project info")
    @PutMapping("/{id}")
    public ProjectDto updateProject(@PathVariable Long id,
                                    @RequestBody UpdateProjectRequestDto requestDto) {
        return projectService.update(id, requestDto);
    }

    @Operation(summary = "Update status", description = "Update project status")
    @PatchMapping("/{id}/status")
    public ProjectDto updateStatus(@PathVariable Long id,
                                   @RequestBody UpdateProjectStatusRequestDto requestDto) {
        return projectService.updateStatus(id, requestDto);
    }

    @Operation(summary = "Assign user", description = "Assign additional user for the project")
    @PutMapping("/{id}/users")
    public ProjectDto assignUser(@PathVariable Long id,
                                 @RequestBody ProjectUserAssignmentRequestDto requestDto) {
        return projectService.addUser(id, requestDto);
    }

    @Operation(summary = "Remove user", description = "Remove user from the project")
    @PatchMapping("/{id}/users")
    public ProjectDto removeUser(@PathVariable Long id,
                                 @RequestBody ProjectUserAssignmentRequestDto requestDto) {
        return projectService.removeUser(id, requestDto);
    }

    @Operation(summary = "Delete project", description = "Delete the project")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }
}
