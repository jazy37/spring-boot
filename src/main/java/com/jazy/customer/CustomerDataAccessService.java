package com.jazy.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@mail.com", 21);
        customers.add(alex);
        Customer jakub = new Customer(2, "Jakub", "jakub@mail.com", 22);
        customers.add(jakub);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> findCustomerById(int id) {
        return customers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }
}
