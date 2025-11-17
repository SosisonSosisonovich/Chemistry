package com.seroter.homework.Entity;

import lombok.*;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer role_id;

    @Column(nullable = false)
    private String role_name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user_id")
    private Set<Users> users;
}
