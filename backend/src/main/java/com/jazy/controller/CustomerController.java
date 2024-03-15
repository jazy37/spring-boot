package com.jazy.controller;

import com.jazy.customer.Customer;
import com.jazy.customer.CustomerRequest;
import com.jazy.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable(name = "id") Integer id){
        return customerService.getCustomerById(id);
    }

    @PostMapping()
    public void registerCustomer(@RequestBody CustomerRequest customer) {
        System.out.printf("customer from body %s%n", customer);
        customerService.addCustomer(customer);
    }
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable(name = "id") Integer id, @RequestBody CustomerRequest customer) {
        customerService.updateCustomer(id, customer);
    }
}
