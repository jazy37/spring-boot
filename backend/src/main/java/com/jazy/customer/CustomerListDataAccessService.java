package com.jazy.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1L, "Alex", "alex@mail.com", "password", 21, Gender.FEMALE);
        customers.add(alex);
        Customer jakub = new Customer(2L, "Jakub", "jakub@mail.com", "password", 22, Gender.MALE);
        customers.add(jakub);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> findCustomerById(long id) {
        return customers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public void saveCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(s -> s.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(long id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean existsCustomerById(long id) {
        return customers.stream().anyMatch(s -> s.getId().equals(id));
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void updateCustomer(Customer customer) {
        Optional<Customer> foundCustomer = customers.stream()
                .filter(c -> c.getId().equals(customer.getId()))
                .findFirst();

        if (foundCustomer.isPresent()) {
            Customer c = foundCustomer.get();
            if (customer.getName() != null) {
                c.setName(customer.getName());
            }
            if (customer.getEmail() != null) {
                c.setEmail(customer.getEmail());
            }
            if (customer.getAge() != null) {
                c.setAge(customer.getAge());
            }
        }
    }
}
