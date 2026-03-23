package com.bank.crm.controller;

import com.bank.crm.entity.MarketingOpportunity;
import com.bank.crm.service.MarketingService;
import com.bank.crm.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 营销机会控制器
 */
@RestController
@RequestMapping("/api/marketing-opportunities")
public class MarketingController {

    @Autowired
    private MarketingService marketingService;

    /**
     * 获取营销机会列表
     */
    @GetMapping("/list")
    public Result<IPage<MarketingOpportunity>> getMarketingList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String status) {
        IPage<MarketingOpportunity> result = marketingService.getMarketingList(current, size, name, source, status);
        return Result.success(result);
    }

    /**
     * 获取营销机会详情
     */
    @GetMapping("/{id}")
    public Result<MarketingOpportunity> getMarketingDetail(@PathVariable Integer id) {
        MarketingOpportunity marketing = marketingService.getMarketingById(id);
        return Result.success(marketing);
    }

    /**
     * 添加营销机会
     */
    @PostMapping
    public Result<MarketingOpportunity> addMarketing(@Valid @RequestBody MarketingOpportunity marketing) {
        MarketingOpportunity result = marketingService.saveMarketing(marketing);
        return Result.success(result);
    }

    /**
     * 更新营销机会
     */
    @PutMapping("/{id}")
    public Result<MarketingOpportunity> updateMarketing(@PathVariable Integer id, @Valid @RequestBody MarketingOpportunity marketing) {
        marketing.setId(id);
        MarketingOpportunity result = marketingService.updateMarketing(marketing);
        return Result.success(result);
    }

    /**
     * 删除营销机会
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteMarketing(@PathVariable Integer id) {
        marketingService.deleteMarketing(id);
        return Result.success("删除成功");
    }

    /**
     * 根据客户ID获取营销机会
     */
    @GetMapping("/customer/{customerId}")
    public Result<List<MarketingOpportunity>> getMarketingByCustomerId(@PathVariable Integer customerId) {
        List<MarketingOpportunity> marketingList = marketingService.getMarketingByCustomerId(customerId);
        return Result.success(marketingList);
    }
}