package manager.ecommerce.repository;

import manager.ecommerce.entity.Role;
import manager.ecommerce.entity.contants.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(RoleEnum roleEnum);
}
