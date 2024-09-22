package mate.academy.taskmanagementsystem.repository.task;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.task.TaskSearchParameters;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationBuilder;
import mate.academy.taskmanagementsystem.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSpecificationBuilder implements SpecificationBuilder<Task, TaskSearchParameters> {
    private final SpecificationProviderManager<Task> taskSpecificationProviderManager;

    @Override
    public Specification<Task> build(TaskSearchParameters searchParameters) {
        Specification<Task> spec = Specification.where(null);
        if (searchParameters.namePart() != null && !searchParameters.namePart().isEmpty()) {
            spec = spec.and(taskSpecificationProviderManager.getSpecificationProvider("name")
                    .getSpecification(searchParameters.namePart()));
        }
        if (searchParameters.priority() != null && !searchParameters.priority().isEmpty()) {
            spec = spec.and(taskSpecificationProviderManager.getSpecificationProvider("priority")
                    .getSpecification(searchParameters.priority()));
        }
        if (searchParameters.status() != null && !searchParameters.status().isEmpty()) {
            spec = spec.and(taskSpecificationProviderManager.getSpecificationProvider("status")
                    .getSpecification(searchParameters.status()));
        }
        if (searchParameters.dueDate() != null) {
            spec = spec.and(taskSpecificationProviderManager.getSpecificationProvider("dueDate")
                    .getSpecification(searchParameters.dueDate()));
        }
        return spec;
    }
}
