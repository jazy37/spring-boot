package com.jazy.repository;

import com.jazy.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
   boolean existsCustomerByEmail(String email);
   boolean existsCustomerById(long id);
   Optional<Customer> findCustomerByEmail(String email);
}
