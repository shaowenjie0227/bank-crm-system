package com.bank.crm.controller;

import com.bank.crm.dto.RecommendationOverviewDTO;
import com.bank.crm.service.RecommendationService;
import com.bank.crm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/dashboard")
    public Result<RecommendationOverviewDTO> getDashboardRecommendations() {
        return Result.success(recommendationService.getDashboardRecommendations());
    }
}
