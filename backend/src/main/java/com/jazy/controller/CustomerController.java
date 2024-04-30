package com.jazy.controller;

import com.jazy.customer.Customer;
import com.jazy.customer.CustomerRequest;
import com.jazy.dto.CustomerDto;
import com.jazy.jwt.JWTUtil;
import com.jazy.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final JWTUtil jwtUtil;


    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public List<CustomerDto> getCustomers() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable(name = "id") Integer id){
        return customerService.getCustomerById(id);
    }

    @PostMapping()
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRequest customer) {
        customerService.addCustomer(customer);
        String jwtToken = jwtUtil.issueToken(customer.email(), "ROLE_USER");
        ResponseCookie cookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(1800)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
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
