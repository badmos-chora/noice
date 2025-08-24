package com.noice.userbff.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions", uniqueConstraints = {@UniqueConstraint(name = "permission_name_unique",columnNames = "name")})
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String defaultRoles;

    @ManyToMany(mappedBy = "permissions")
    private Set<User> users = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Permission that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
