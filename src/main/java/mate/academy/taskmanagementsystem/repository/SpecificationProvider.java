package mate.academy.taskmanagementsystem.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    default Specification<T> getSpecification(String param) {
        throw new UnsupportedOperationException("Can't get specification with such "
                + "input parameter. Method is not implemented");
    }

    default Specification<T> getSpecification(LocalDate param) {
        throw new UnsupportedOperationException("Can't get specification with such "
                + "input parameter. Method is not implemented");
    }
}
