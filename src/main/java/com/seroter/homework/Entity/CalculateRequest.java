package com.seroter.homework.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalculateRequest {

    private Integer material_id;

    @JsonProperty("Pg")
    private double Pg;
    @JsonProperty("T")
    private double T;
}
