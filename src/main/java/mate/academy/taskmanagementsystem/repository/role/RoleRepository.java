package mate.academy.taskmanagementsystem.repository.role;

import mate.academy.taskmanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(Role.RoleName roleName);
}
