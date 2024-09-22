package mate.academy.taskmanagementsystem.repository.task;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.SpecificationProvider;
import mate.academy.taskmanagementsystem.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSpecificationProviderManager implements SpecificationProviderManager<Task> {
    private final List<SpecificationProvider<Task>> taskSpecificationProviders;

    @Override
    public SpecificationProvider<Task> getSpecificationProvider(String key) {
        return taskSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provider for key: " + key));
    }
}
