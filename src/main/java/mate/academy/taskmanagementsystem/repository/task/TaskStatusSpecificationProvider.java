package mate.academy.taskmanagementsystem.repository.task;

import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusSpecificationProvider implements SpecificationProvider<Task> {
    private static final String STATUS = "status";

    @Override
    public String getKey() {
        return STATUS;
    }

    @Override
    public Specification<Task> getSpecification(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(STATUS), status);
    }
}
