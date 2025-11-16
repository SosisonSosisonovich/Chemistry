package com.seroter.homework.Entity;

import lombok.*;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer user_id;

    private String username;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

}
