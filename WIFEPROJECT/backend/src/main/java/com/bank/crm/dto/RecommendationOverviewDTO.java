package com.bank.crm.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecommendationOverviewDTO {
    private String username;
    private String algorithm;
    private String summary;
    private String generatedAt;
    private Integer interactionCount;
    private List<RecommendationItemDTO> recommendations = new ArrayList<>();
}
