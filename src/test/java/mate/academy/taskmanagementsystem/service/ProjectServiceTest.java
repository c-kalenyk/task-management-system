package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestCreateProjectRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProject;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProjectDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProjectUserAssignmentRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestUpdateProjectRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestUpdateProjectStatusRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.updateProjectInfo;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createAdditionalTestUser;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUsersSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import mate.academy.taskmanagementsystem.service.internal.impl.ProjectServiceImpl;
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
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("Verify that correct ProjectDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnCorrectProjectDto() {
        //Given
        Project project = createTestProject();
        CreateProjectRequestDto requestDto = createTestCreateProjectRequestDto(project);
        ProjectDto expected = createTestProjectDto(project);
        User user = project.getUsers().iterator().next();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(projectMapper.toModel(requestDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.create(user.getId(), requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(user.getId());
        verify(projectMapper, times(1)).toModel(requestDto);
        verify(projectRepository, times(1)).save(project);
        verify(projectMapper, times(1)).toDto(project);
        verifyNoMoreInteractions(userRepository, projectMapper, projectRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            with invalid user id""")
    public void create_WithInvalidUserId_ShouldThrowException() {
        //Given
        CreateProjectRequestDto requestDto = createTestCreateProjectRequestDto(createTestProject());
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> projectService.create(id, requestDto)
        );
        //Then
        String expected = "Can't find user by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct Project set was returned when calling
            getAllProjects() method""")
    public void getAllProjects_WithValidUserId_ShouldReturnCorrectProjectSet() {
        //Given
        Project project = createTestProject();
        ProjectDto projectDto = createTestProjectDto(project);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Project> projectPage = new PageImpl<>(List.of(project), pageable, 1);
        Set<ProjectDto> expected = Set.of(projectDto);
        User user = project.getUsers().iterator().next();

        when(projectRepository.findAllByUserId(user.getId(), pageable)).thenReturn(projectPage);
        when(projectMapper.toDtoSet(projectPage.getContent())).thenReturn(expected);
        //When
        Set<ProjectDto> actual = projectService.getAllProjects(user.getId(), pageable);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findAllByUserId(user.getId(), pageable);
        verify(projectMapper, times(1)).toDtoSet(projectPage.getContent());
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    @DisplayName("Verify that correct ProjectDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidProjectDto() {
        //Given
        Project project = createTestProject();
        ProjectDto expected = createTestProjectDto(project);
        Long id = project.getId();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verify(projectMapper, times(1)).toDto(project);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> projectService.get(id)
        );
        //Then
        String expected = "Can't find project by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    @DisplayName("Verify that correct ProjectDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidProjectDto() {
        //Given
        Project existingProject = createTestProject();
        UpdateProjectRequestDto requestDto = createTestUpdateProjectRequestDto();
        Project updatedProject = updateProjectInfo(requestDto, existingProject);
        ProjectDto expected = createTestProjectDto(updatedProject);
        Long id = expected.getId();

        when(projectRepository.findById(id)).thenReturn(Optional.of(existingProject));
        when(projectMapper.updateProject(existingProject, requestDto)).thenReturn(updatedProject);
        when(projectRepository.save(updatedProject)).thenReturn(updatedProject);
        when(projectMapper.toDto(updatedProject)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verify(projectMapper, times(1)).updateProject(existingProject, requestDto);
        verify(projectRepository, times(1)).save(updatedProject);
        verify(projectMapper, times(1)).toDto(updatedProject);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    @DisplayName("Verify that correct ProjectDto was returned when calling updateStatus() method")
    public void updateStatus_WithValidRequestDto_ShouldReturnValidProjectDto() {
        //Given
        Project project = createTestProject();
        UpdateProjectStatusRequestDto requestDto = createTestUpdateProjectStatusRequestDto();
        project.setStatus(requestDto.getStatus());
        ProjectDto expected = createTestProjectDto(project);
        Long id = expected.getId();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.updateStatus(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verify(projectRepository, times(1)).save(project);
        verify(projectMapper, times(1)).toDto(project);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    @DisplayName("Verify that user was assigned to the project when calling addUser() method")
    public void addUser_WithValidRequestDto_ShouldReturnValidProjectDto() {
        //Given
        Project project = createTestProject();
        ProjectUserAssignmentRequestDto requestDto = createTestProjectUserAssignmentRequestDto();
        User newUser = createAdditionalTestUser();
        project.setUsers(createTestUsersSet());
        ProjectDto expected = createTestProjectDto(project);
        Long id = expected.getId();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(newUser));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.addUser(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findById(requestDto.getUserId());
        verify(projectRepository, times(1)).save(project);
        verify(projectMapper, times(1)).toDto(project);
        verifyNoMoreInteractions(projectRepository, userRepository, projectMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling addUser() method
            with already assigned user""")
    public void addUser_WithDuplicateUser_ShouldThrowException() {
        //Given
        Project project = createTestProject();
        ProjectUserAssignmentRequestDto requestDto = createTestProjectUserAssignmentRequestDto();
        User user = project.getUsers().iterator().next();
        requestDto.setUserId(user.getId());

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(user));
        //When
        Exception exception = assertThrows(
                EntityExistsException.class,
                () -> projectService.addUser(project.getId(), requestDto)
        );
        //Then
        String expected = "User " + user.getUsername() + " is already assigned for this project";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(project.getId());
        verify(userRepository, times(1)).findById(requestDto.getUserId());
        verifyNoMoreInteractions(projectRepository, userRepository);
    }

    @Test
    @DisplayName("Verify that user was removed from the project when calling removeUser() method")
    public void removeUser_WithValidRequestDto_ShouldReturnValidProjectDto() {
        //Given
        Project project = createTestProject();
        final ProjectUserAssignmentRequestDto requestDto
                = createTestProjectUserAssignmentRequestDto();
        User newUser = createAdditionalTestUser();
        project.setUsers(createTestUsersSet());
        project.getUsers().remove(newUser);
        ProjectDto expected = createTestProjectDto(project);
        Long id = expected.getId();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(newUser));
        doNothing().when(projectRepository).removeUserFromProject(project.getId(), newUser.getId());
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(expected);
        //When
        ProjectDto actual = projectService.removeUser(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(projectRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findById(requestDto.getUserId());
        verify(projectRepository, times(1)).removeUserFromProject(project.getId(), newUser.getId());
        verify(projectRepository, times(1)).save(project);
        verify(projectMapper, times(1)).toDto(project);
        verifyNoMoreInteractions(projectRepository, userRepository, projectMapper);
    }
}
