package com.seroter.homework.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "material")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer material_id;

    @Column(nullable = false)
    private String material_name;

    @Column(nullable = false)
    private double a0;

    @Column(nullable = false)
    private double a1;

    @Column(nullable = false)
    private double a2;

    @Column(nullable = false)
    private double a3;

    @Column(nullable = false)
    private double a4;

    @Column(nullable = false)
    private double a5;
}
