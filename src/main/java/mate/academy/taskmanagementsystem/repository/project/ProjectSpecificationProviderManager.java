package mate.academy.taskmanagementsystem.repository.project;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.model.Project;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import mate.academy.taskmanagementsystem.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectSpecificationProviderManager implements SpecificationProviderManager<Project> {
    private final List<SpecificationProvider<Project>> projectSpecificationProviders;

    @Override
    public SpecificationProvider<Project> getSpecificationProvider(String key) {
        return projectSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provider for key: " + key));
    }
}
