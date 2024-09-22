package mate.academy.taskmanagementsystem.repository.task;

import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskNameSpecificationProvider implements SpecificationProvider<Task> {
    private static final String NAME = "name";

    @Override
    public String getKey() {
        return NAME;
    }

    @Override
    public Specification<Task> getSpecification(String namePart) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), "%" + namePart + "%");
    }
}
