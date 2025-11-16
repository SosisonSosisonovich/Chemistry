package com.seroter.homework.Service;

import com.seroter.homework.Entity.Material;
import com.seroter.homework.Repository.MaterialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepo materialRepo;

    public List<Material> getAllMaterials(){
        return materialRepo.findAll();
    }

    public Optional<Material> getMaterial(Integer id){
        return materialRepo.findById(id);
    }

    public Material addNewMaterial(
            String name,
            double a0, double a1, double a2,
            double a3, double a4, double a5
    ) {

        Material material = new Material();
        material.setMaterial_name(name);
        material.setA0(a0);
        material.setA1(a1);
        material.setA2(a2);
        material.setA3(a3);
        material.setA4(a4);
        material.setA5(a5);

        return materialRepo.save(material);
    }

    public Material updateMaterial(
            Integer id, String name,
            Double a0, Double a1,
            Double a2, Double a3,
            Double a4, Double a5) {

        return materialRepo.findById(id).map(existingMaterial -> {

            if (name != null) existingMaterial.setMaterial_name(name);
            if (a0 != null) existingMaterial.setA0(a0);
            if (a1 != null) existingMaterial.setA1(a1);
            if (a2 != null) existingMaterial.setA2(a2);
            if (a3 != null) existingMaterial.setA3(a3);
            if (a4 != null) existingMaterial.setA4(a4);
            if (a5 != null) existingMaterial.setA5(a5);

            return materialRepo.save(existingMaterial);

        }).orElseThrow(() -> new RuntimeException("Material not found with id " + id));
    }

    public void deleteMaterial(Integer id){
        Material m = materialRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Материал не найден."));
        materialRepo.delete(m);
    }
}
