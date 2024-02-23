package com.jazy.service;

import com.jazy.customer.Customer;
import com.jazy.customer.CustomerDao;
import com.jazy.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getCustomerDao() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id){
        return customerDao.findCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer [%s] not found".formatted(id)));
    }
}
