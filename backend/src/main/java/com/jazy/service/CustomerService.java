package com.jazy.service;

import com.jazy.customer.Customer;
import com.jazy.customer.CustomerDao;
import com.jazy.customer.CustomerRequest;
import com.jazy.exception.CustomerDuplicateException;
import com.jazy.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(long id){
        return customerDao.findCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer id: %s not found".formatted(id)));
    }

    public void addCustomer(CustomerRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)) {
            throw new CustomerDuplicateException("Customer with email %s already exists".formatted(email));
        }

        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.saveCustomer(customer);
    }

    public void deleteCustomer(long id) {
        if (!customerDao.existsCustomerById(id)) {
            throw new CustomerNotFoundException("Customer id: %s not found".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(long id, CustomerRequest customerRequest) {
        Customer customer = getCustomerById(id);

        if(customerRequest.age() != null)
            customer.setAge(customerRequest.age());
        if(customerRequest.email() != null) {
            if(!customerRequest.email().equals(customer.getEmail()) && customerDao.existsPersonWithEmail(customerRequest.email())) {
                throw new CustomerDuplicateException("customer email %s already exists".formatted(customerRequest.email()));
            }
            customer.setEmail(customerRequest.email());
        }
        if(customerRequest.name() != null)
            customer.setName(customerRequest.name());

        customerDao.updateCustomer(customer);
    }
}
