package com.jazy.controller;

import com.jazy.customer.Customer;
import com.jazy.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomerDao();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable(name = "id") Integer id){
        return customerService.getCustomerById(id);
    }
}
