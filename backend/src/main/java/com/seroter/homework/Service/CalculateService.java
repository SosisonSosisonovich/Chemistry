package com.seroter.homework.Service;

import com.seroter.homework.Entity.Material;
import com.seroter.homework.Repository.MaterialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {

    @Autowired
    private MaterialRepo materialRepo;

    public double MathModel(Integer id, double Pg, double T){

        Material m = materialRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Материал не найден"));

        return m.getA0() + (m.getA1() * Pg) +
                (m.getA2() * T) + (m.getA3() * Pg * T) +
                (m.getA4() * T * T) + (m.getA5() * Pg * T * T);
    }
}
