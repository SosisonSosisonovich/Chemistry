package com.seroter.homework.Repository;

import com.seroter.homework.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends JpaRepository<Roles, Integer> {
    Optional<Roles> findById(Integer id);
}
