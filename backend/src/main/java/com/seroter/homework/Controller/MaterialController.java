package com.seroter.homework.Controller;

import com.seroter.homework.Entity.Material;
import com.seroter.homework.Entity.MaterialRequest;
import com.seroter.homework.Service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> getAllMaterials(){
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterial(@PathVariable Integer id){
        return materialService.getMaterial(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Integer id,
                                                  @RequestBody MaterialRequest m){
        Material updated = materialService.updateMaterial(
                id, m.getMaterial_name(),
                m.getA0(), m.getA1(),
                m.getA2(), m.getA3(),
                m.getA4(), m.getA5()
                );
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/add")
    public ResponseEntity<Material> addMaterial(@RequestBody MaterialRequest request){
        Material m = materialService.addNewMaterial(
                request.getMaterial_name(),
                request.getA0(), request.getA1(),
                request.getA2(), request.getA3(),
                request.getA4(), request.getA5()
        );
        return  ResponseEntity.ok(m);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Integer id){
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }
}
