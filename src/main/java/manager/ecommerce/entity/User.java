package manager.ecommerce.entity;

import manager.ecommerce.entity.base.BaseObject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseObject {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "is_verified", nullable = false)
    Boolean isVerified;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "otp")
    private String otp;

    @Column(name = "is_time_expired")
    private LocalDateTime isTimeExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
