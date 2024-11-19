package com.example.logiXpert.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static com.example.logiXpert.model.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN, EMPLOYEE, CLIENT
//    ADMIN(
//            Set.of(
//                    ADMIN_READ,
//                    ADMIN_CREATE,
//                    ADMIN_DELETE,
//                    ADMIN_WRITE,
//                    EMPLOYEE_READ,
//                    EMPLOYEE_CREATE,
//                    EMPLOYEE_DELETE,
//                    EMPLOYEE_WRITE
//            )
//    ),
//    EMPLOYEE(
//            Set.of(
//                    EMPLOYEE_READ,
//                    EMPLOYEE_CREATE,
//                    EMPLOYEE_DELETE,
//                    EMPLOYEE_WRITE
//            )
//    ),
//    CLIENT(
//            Set.of(
//                    CLIENT_READ,
//                    CLIENT_CREATE,
//                    CLIENT_DELETE,
//                    CLIENT_WRITE
//            )
//    );
//
//    private final Set<Permission> permissions;
//
//    public List<SimpleGrantedAuthority> getAuthorities() {
//        var authorities = getPermissions().stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.name()))
//                .toList();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return authorities;
//    }
}
