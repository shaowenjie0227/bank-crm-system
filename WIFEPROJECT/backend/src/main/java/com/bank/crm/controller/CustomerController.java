package com.bank.crm.controller;

import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.entity.User;
import com.bank.crm.service.AuthService;
import com.bank.crm.service.CustomerService;
import com.bank.crm.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthService authService;

    @GetMapping("/list")
    public Result<IPage<CustomerInfo>> getCustomerList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level) {
        User currentUser = authService.getCurrentUser();
        String managerFilter = isSales(currentUser) ? currentUser.getUsername() : null;
        IPage<CustomerInfo> result = customerService.getCustomerList(current, size, name, type, level, managerFilter);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<CustomerInfo> getCustomerDetail(@PathVariable Integer id) {
        CustomerInfo customer = customerService.getCustomerById(id);
        ensureCustomerAccess(customer);
        return Result.success(customer);
    }

    @PostMapping
    public Result<CustomerInfo> addCustomer(@Valid @RequestBody CustomerInfo customer) {
        User currentUser = authService.getCurrentUser();
        if (isSales(currentUser)) {
            customer.setCustomerManager(currentUser.getUsername());
        }
        CustomerInfo result = customerService.saveCustomer(customer);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<CustomerInfo> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerInfo customer) {
        User currentUser = authService.getCurrentUser();
        CustomerInfo existing = customerService.getCustomerById(id);
        ensureCustomerAccess(existing);
        customer.setId(id);
        if (isSales(currentUser)) {
            customer.setCustomerManager(currentUser.getUsername());
        }
        CustomerInfo result = customerService.updateCustomer(customer);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCustomer(@PathVariable Integer id) {
        CustomerInfo existing = customerService.getCustomerById(id);
        ensureCustomerAccess(existing);
        customerService.deleteCustomer(id);
        return Result.success("删除成功");
    }

    private void ensureCustomerAccess(CustomerInfo customer) {
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        User currentUser = authService.getCurrentUser();
        if (isSales(currentUser) && !currentUser.getUsername().equals(customer.getCustomerManager())) {
            throw new RuntimeException("不允许访问");
        }
    }

    private boolean isSales(User user) {
        return user != null && "销售".equals(user.getRole());
    }
}