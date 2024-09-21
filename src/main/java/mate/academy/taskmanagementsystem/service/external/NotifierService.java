package mate.academy.taskmanagementsystem.service.external;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.repository.project.ProjectRepository;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifierService {
    private final EmailService emailService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Scheduled(cron = "0 0 10 * * ?")
    public void notifyUpcomingDeadlinesForProjects() {
        List<Project> projects = projectRepository.findAllByEndDate(LocalDate.now().plusDays(1));
        for (Project project : projects) {
            for (User user : project.getUsers()) {
                String message = "Reminder: The deadline for project '"
                        + project.getName() + "' is tomorrow.";
                emailService.sendEmail(user.getEmail(),
                        "Project Deadline Reminder", message);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyUpcomingDeadlinesForTasks() {
        List<Task> tasks = taskRepository.findAllByDueDate(LocalDate.now().plusDays(1));
        for (Task task : tasks) {
            String message = "Reminder: The deadline for task '"
                    + task.getName() + "' is tomorrow.";
            emailService.sendEmail(task.getAssignee().getEmail(),
                    "Task Deadline Reminder", message);
        }
    }
}
