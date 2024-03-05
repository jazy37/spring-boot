package com.jazy.repository;

import com.jazy.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
   boolean existsCustomerByEmail(String email);
   boolean existsCustomerById(long id);
}
