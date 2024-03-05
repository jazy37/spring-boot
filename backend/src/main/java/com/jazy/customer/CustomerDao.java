package com.jazy.customer;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> findCustomerById(long id);
    void saveCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsCustomerById(long id);
    void deleteCustomer(long id);
    void updateCustomer(Customer customer);


}
