package com.seroter.homework.Repository;

import com.seroter.homework.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);
    Users findByUsername(String username);
}
