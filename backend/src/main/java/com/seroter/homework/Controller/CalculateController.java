package com.seroter.homework.Controller;

import com.seroter.homework.Entity.CalculateRequest;
import com.seroter.homework.Entity.Material;
import com.seroter.homework.Repository.MaterialRepo;
import com.seroter.homework.Service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calculate")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @PostMapping
    public Map<String, Double> calculate(@RequestBody CalculateRequest request){
        double p = calculateService.MathModel(request.getMaterial_id(),
                request.getPg(),
                request.getT());
        return Map.of("p", p);
    }


}
