package mate.academy.taskmanagementsystem.repository.task;

import java.time.LocalDate;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskDateSpecificationProvider implements SpecificationProvider<Task> {
    private static final String DUE_DATE = "dueDate";

    @Override
    public String getKey() {
        return DUE_DATE;
    }

    @Override
    public Specification<Task> getSpecification(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(DUE_DATE), endDate);
    }
}
