package com.jazy.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> findCustomerById(int id);
}
