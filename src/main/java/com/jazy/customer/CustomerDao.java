package com.jazy.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> findCustomerById(int id);
    void saveCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsCustomerById(int id);
    void deleteCustomer(int id);
    void updateCustomer(Customer customer);


}
