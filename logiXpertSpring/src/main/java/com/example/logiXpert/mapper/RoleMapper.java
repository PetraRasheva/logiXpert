package com.example.logiXpert.mapper;

import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Role;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default ERole toERole(Role role) {
        return role != null ? role.getName() : null; // Map Role to its ERole value
    }

    default Set<ERole> toERoleSet(Set<Role> roles) {
        return roles != null ?
                roles.stream()
                        .map(this::toERole)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();
    }
}
