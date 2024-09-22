package mate.academy.taskmanagementsystem.repository.project;

import java.time.LocalDate;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProjectDateSpecificationProvider implements SpecificationProvider<Project> {
    private static final String END_DATE = "endDate";

    @Override
    public String getKey() {
        return END_DATE;
    }

    @Override
    public Specification<Project> getSpecification(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(END_DATE), endDate);
    }
}
