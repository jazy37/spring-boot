package com.jazy.service;

import com.jazy.customer.*;
import com.jazy.dto.CustomerDTOMapper;
import com.jazy.dto.CustomerDto;
import com.jazy.exception.CustomerDuplicateException;
import com.jazy.exception.CustomerNotFoundException;
import com.jazy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;
    private final RoleRepository roleRepository;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao,
                           PasswordEncoder passwordEncoder,
                           CustomerDTOMapper customerDTOMapper,
                           RoleRepository roleRepository) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
        this.roleRepository = roleRepository;
    }

    public List<CustomerDto> getAllCustomer() {
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(long id){
        return customerDao.findCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new CustomerNotFoundException("Customer id: %s not found".formatted(id)));
    }

    public void addCustomer(CustomerRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)) {
            throw new CustomerDuplicateException("Customer with email %s already exists".formatted(email));
        }
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER);
        roles.add(role);
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );
        customer.setRoles(roles);
        customerDao.saveCustomer(customer);
    }

    public void deleteCustomer(long id) {
        if (!customerDao.existsCustomerById(id)) {
            throw new CustomerNotFoundException("Customer id: %s not found".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(long id, CustomerRequest customerRequest) {
        Customer customer = customerDao.findCustomerById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer id: %s not found".formatted(id)));

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
