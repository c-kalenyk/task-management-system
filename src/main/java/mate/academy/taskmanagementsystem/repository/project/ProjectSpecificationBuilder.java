package mate.academy.taskmanagementsystem.repository.project;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.project.ProjectSearchParameters;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.SpecificationBuilder;
import mate.academy.taskmanagementsystem.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectSpecificationBuilder
        implements SpecificationBuilder<Project, ProjectSearchParameters> {
    private final SpecificationProviderManager<Project> projectSpecificationProviderManager;

    @Override
    public Specification<Project> build(ProjectSearchParameters searchParameters) {
        Specification<Project> spec = Specification.where(null);
        if (searchParameters.namePart() != null && !searchParameters.namePart().isEmpty()) {
            spec = spec.and(projectSpecificationProviderManager.getSpecificationProvider("name")
                    .getSpecification(searchParameters.namePart()));
        }
        if (searchParameters.endDate() != null) {
            spec = spec.and(projectSpecificationProviderManager.getSpecificationProvider("endDate")
                    .getSpecification(searchParameters.endDate()));
        }
        if (searchParameters.status() != null && !searchParameters.status().isEmpty()) {
            spec = spec.and(projectSpecificationProviderManager.getSpecificationProvider("status")
                    .getSpecification(searchParameters.status()));
        }
        return spec;
    }
}
