package com.bank.crm.controller;

import com.bank.crm.entity.CustomerDevelopment;
import com.bank.crm.service.DevelopmentService;
import com.bank.crm.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户开发控制器
 */
@RestController
@RequestMapping("/api/developments")
public class DevelopmentController {

    @Autowired
    private DevelopmentService developmentService;

    /**
     * 获取客户开发列表
     */
    @GetMapping("/list")
    public Result<IPage<CustomerDevelopment>> getDevelopmentList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String progress,
            @RequestParam(required = false) String status) {
        IPage<CustomerDevelopment> result = developmentService.getDevelopmentList(current, size, customerName, progress, status);
        return Result.success(result);
    }

    /**
     * 获取客户开发详情
     */
    @GetMapping("/{id}")
    public Result<CustomerDevelopment> getDevelopmentDetail(@PathVariable Integer id) {
        CustomerDevelopment development = developmentService.getDevelopmentById(id);
        return Result.success(development);
    }

    /**
     * 添加客户开发
     */
    @PostMapping
    public Result<CustomerDevelopment> addDevelopment(@Valid @RequestBody CustomerDevelopment development) {
        CustomerDevelopment result = developmentService.saveDevelopment(development);
        return Result.success(result);
    }

    /**
     * 更新客户开发
     */
    @PutMapping("/{id}")
    public Result<CustomerDevelopment> updateDevelopment(@PathVariable Integer id, @Valid @RequestBody CustomerDevelopment development) {
        development.setId(id);
        CustomerDevelopment result = developmentService.updateDevelopment(development);
        return Result.success(result);
    }

    /**
     * 删除客户开发
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteDevelopment(@PathVariable Integer id) {
        developmentService.deleteDevelopment(id);
        return Result.success("删除成功");
    }

    /**
     * 根据客户ID获取客户开发
     */
    @GetMapping("/customer/{customerId}")
    public Result<List<CustomerDevelopment>> getDevelopmentByCustomerId(@PathVariable Integer customerId) {
        List<CustomerDevelopment> developmentList = developmentService.getDevelopmentByCustomerId(customerId);
        return Result.success(developmentList);
    }
}