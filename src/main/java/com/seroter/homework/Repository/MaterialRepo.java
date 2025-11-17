package com.seroter.homework.Repository;

import com.seroter.homework.Entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepo extends JpaRepository<Material, Integer> {
    Optional<Material> findById(Integer id);
}
