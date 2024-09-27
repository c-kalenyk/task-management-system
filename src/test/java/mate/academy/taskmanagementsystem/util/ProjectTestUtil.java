package mate.academy.taskmanagementsystem.util;

import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUser;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUserResponseDto;

import java.time.LocalDate;
import java.util.Set;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectUserAssignmentRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectStatusRequestDto;
import mate.academy.taskmanagementsystem.model.Project;

public class ProjectTestUtil {
    public static Project createTestProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("name");
        project.setDescription("description");
        project.setStartDate(LocalDate.of(2024, 9, 26));
        project.setEndDate(LocalDate.of(2024, 10, 26));
        project.setStatus(Project.Status.INITIATED);
        project.setUsers(Set.of(createTestUser()));
        return project;
    }

    public static CreateProjectRequestDto createTestCreateProjectRequestDto(Project project) {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName(project.getName());
        requestDto.setDescription(project.getDescription());
        requestDto.setStartDate(project.getStartDate());
        requestDto.setEndDate(project.getEndDate());
        return requestDto;
    }

    public static UpdateProjectRequestDto createTestUpdateProjectRequestDto() {
        UpdateProjectRequestDto requestDto = new UpdateProjectRequestDto();
        requestDto.setName("upd name");
        requestDto.setDescription("upd description");
        requestDto.setStartDate(LocalDate.of(2024, 10, 1));
        requestDto.setEndDate(LocalDate.of(2024, 10, 30));
        return requestDto;
    }

    public static UpdateProjectStatusRequestDto createTestUpdateProjectStatusRequestDto() {
        UpdateProjectStatusRequestDto requestDto = new UpdateProjectStatusRequestDto();
        requestDto.setStatus(Project.Status.IN_PROGRESS);
        return requestDto;
    }

    public static ProjectUserAssignmentRequestDto createTestProjectUserAssignmentRequestDto() {
        ProjectUserAssignmentRequestDto requestDto = new ProjectUserAssignmentRequestDto();
        requestDto.setUserId(2L);
        return requestDto;
    }

    public static ProjectDto createTestProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setStatus(project.getStatus().toString());
        projectDto.setUsers(Set.of(
                createTestUserResponseDto(project.getUsers().iterator().next())));
        return projectDto;
    }

    public static Project updateProjectInfo(UpdateProjectRequestDto requestDto,
                                            Project project) {
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        return project;
    }
}
