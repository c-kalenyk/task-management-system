package mate.academy.taskmanagementsystem.repository.project;

import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProjectStatusSpecificationProvider implements SpecificationProvider<Project> {
    private static final String STATUS = "status";

    @Override
    public String getKey() {
        return STATUS;
    }

    @Override
    public Specification<Project> getSpecification(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(STATUS), status);
    }
}
