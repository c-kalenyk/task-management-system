package mate.academy.taskmanagementsystem.repository.project;

import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProjectNameSpecificationProvider implements SpecificationProvider<Project> {
    private static final String NAME = "name";

    @Override
    public String getKey() {
        return NAME;
    }

    @Override
    public Specification<Project> getSpecification(String namePart) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), "%" + namePart + "%");
    }
}
