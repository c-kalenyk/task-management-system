package mate.academy.taskmanagementsystem.repository.task;

import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskPrioritySpecificationProvider implements SpecificationProvider<Task> {
    private static final String PRIORITY = "priority";

    @Override
    public String getKey() {
        return PRIORITY;
    }

    @Override
    public Specification<Task> getSpecification(String priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(PRIORITY), priority);
    }
}
