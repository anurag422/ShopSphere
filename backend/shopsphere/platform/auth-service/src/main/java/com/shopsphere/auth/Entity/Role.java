package com.shopsphere.auth.Entity;


import com.shopsphere.auth.Enum.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleType name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
