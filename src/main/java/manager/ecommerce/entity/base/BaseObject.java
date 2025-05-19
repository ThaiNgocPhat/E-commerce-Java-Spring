package manager.ecommerce.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Getter
@Setter
public class BaseObject
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
