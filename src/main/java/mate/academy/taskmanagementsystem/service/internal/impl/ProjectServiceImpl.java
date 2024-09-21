package mate.academy.taskmanagementsystem.service.internal.impl;

import jakarta.persistence.EntityExistsException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectUserAssignmentRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectStatusRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.ProjectMapper;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.project.ProjectRepository;
import mate.academy.taskmanagementsystem.repository.user.UserRepository;
import mate.academy.taskmanagementsystem.service.internal.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    public ProjectDto create(Long userId, CreateProjectRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Project project = projectMapper.toModel(requestDto);
        project.setUsers(Set.of(user));
        project.setStatus(Project.Status.INITIATED);
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public Set<ProjectDto> getAllProjects(Long userId, Pageable pageable) {
        Page<Project> projectsPage = projectRepository.findAllByUserId(userId, pageable);
        return projectMapper.toDtoSet(projectsPage.getContent());
    }

    @Override
    public ProjectDto get(Long id) {
        return projectMapper.toDto(projectRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: " + id)));
    }

    @Override
    public ProjectDto update(Long id, UpdateProjectRequestDto requestDto) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: " + id));
        Project updatedProject = projectMapper.updateProject(existingProject, requestDto);
        return projectMapper.toDto(projectRepository.save(updatedProject));
    }

    @Override
    public ProjectDto updateStatus(Long id, UpdateProjectStatusRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: " + id));
        project.setStatus(requestDto.getStatus());
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto addUser(Long id, ProjectUserAssignmentRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: " + id));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + requestDto.getUserId()));
        Set<User> users = project.getUsers();
        if (!users.contains(user)) {
            users.add(user);
            return projectMapper.toDto(projectRepository.save(project));
        } else {
            throw new EntityExistsException("User " + user.getUsername()
                    + " is already assigned for this project");
        }
    }

    @Transactional
    @Override
    public ProjectDto removeUser(Long id, ProjectUserAssignmentRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find project by id: " + id));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + requestDto.getUserId()));
        project.getUsers().remove(user);
        projectRepository.removeUserFromProject(project.getId(), user.getId());
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
