package com.noice.userbff.entity;


import com.noice.userbff.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String password;
    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 7, max = 20)
    private String phoneNumber;
    @Builder.Default
    private Boolean enabled=true;

    @ManyToMany
    @JoinTable(name = "users_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id"))
    @Builder.Default
    private Set<Permission> permissions = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdDate;
    @UpdateTimestamp
    private Instant updatedDate;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
