package mate.academy.taskmanagementsystem.util;

import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProject;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.model.Task;

public class TaskTestUtil {
    public static Task createTestTask() {
        Task task = new Task();
        task.setId(1L);
        task.setName("name");
        task.setDescription("description");
        task.setPriority(Task.Priority.LOW);
        task.setStatus(Task.Status.NOT_STARTED);
        task.setDueDate(LocalDate.of(2024, 10, 26));
        task.setProject(createTestProject());
        task.setAssignee(createTestUser());
        return task;
    }

    public static TaskRequestDto createTestTaskRequestDto(Task task) {
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setName(task.getName());
        requestDto.setDescription(task.getDescription());
        requestDto.setPriority(task.getPriority());
        requestDto.setStatus(task.getStatus());
        requestDto.setDueDate(task.getDueDate());
        requestDto.setProjectId(task.getProject().getId());
        if (task.getAssignee() != null) {
            requestDto.setAssigneeId(task.getAssignee().getId());
        } else {
            requestDto.setAssigneeId(null);
        }
        return requestDto;
    }

    public static TaskDto createTestTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setPriority(task.getPriority().toString());
        taskDto.setStatus(task.getStatus().toString());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setProjectId(task.getProject().getId());
        if (task.getAssignee() != null) {
            taskDto.setAssigneeId(task.getAssignee().getId());
        } else {
            taskDto.setAssigneeId(null);
        }
        return taskDto;
    }

    public static List<TaskDto> fillExpectedTaskDtoList() {
        List<TaskDto> expected = new ArrayList<>();
        expected.add(new TaskDto().setId(1L).setName("task 1").setDescription("description 1")
                .setPriority(Task.Priority.LOW.toString())
                .setStatus(Task.Status.NOT_STARTED.toString())
                .setDueDate(LocalDate.of(2024, 10, 26))
                .setProjectId(1L).setAssigneeId(1L));
        expected.add(new TaskDto().setId(2L).setName("task 2").setDescription("description 2")
                .setPriority(Task.Priority.MEDIUM.toString())
                .setStatus(Task.Status.IN_PROGRESS.toString())
                .setDueDate(LocalDate.of(2024, 10, 24))
                .setProjectId(1L).setAssigneeId(1L));
        return expected;
    }
}
