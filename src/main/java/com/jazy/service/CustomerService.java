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

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getCustomerDao() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id){
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

    public void deleteCustomer(int id) {
        if (!customerDao.existsCustomerById(id)) {
            throw new CustomerNotFoundException("Customer id: %s not found".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(int id, CustomerRequest customerRequest) {
        Customer customer = getCustomerById(id);

        if(customerRequest.age() != null)
            customer.setAge(customerRequest.age());
        if(customerRequest.email() != null)
            customer.setEmail(customerRequest.email());
        if(customerRequest.name() != null)
            customer.setName(customerRequest.name());

        customerDao.updateCustomer(customer);
    }
}
