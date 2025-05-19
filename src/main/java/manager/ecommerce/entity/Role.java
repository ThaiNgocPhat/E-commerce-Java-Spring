package manager.ecommerce.entity;
import jakarta.persistence.*;
import manager.ecommerce.entity.base.BaseObject;
import manager.ecommerce.entity.contants.RoleEnum;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "role")
public class Role extends BaseObject {
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
