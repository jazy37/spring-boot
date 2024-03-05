package com.jazy.service;

import com.jazy.customer.Customer;
import com.jazy.customer.CustomerDao;
import com.jazy.customer.CustomerRequest;
import com.jazy.exception.CustomerDuplicateException;
import com.jazy.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomer() {
        //When
        underTest.getAllCustomer();

        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        //Given
        long id =1;
        Customer customer = new Customer(
                "Jakub",
                "jakub@email.com",
                21
        );
        when(customerDao.findCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomerById(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void canNotGetCustomerById() {
        //Given
        long id =1;
        when(customerDao.findCustomerById(id))
                .thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer id: %s not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "jakub@email.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRequest request = new CustomerRequest(
                "Jakub",
                email,
                21
        );

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).saveCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomerWhenEmailExists() {
        //Given
        String email = "jakub@email.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRequest request = new CustomerRequest(
                "Jakub",
                email,
                21
        );

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(CustomerDuplicateException.class)
                .hasMessage("Customer with email %s already exists".formatted(email));

        //Then
        verify(customerDao, never()).saveCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        long id = 10;
        when(customerDao.existsCustomerById(id)).thenReturn(true);

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerDao).deleteCustomer(id);

    }

    @Test
    void willThrowExceptionWhenDeleteCustomer() {
        //Given
        long id = 10;
        when(customerDao.existsCustomerById(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer id: %s not found".formatted(id));

        //Then
        verify(customerDao, never()).deleteCustomer(anyLong());

    }

    @Test
    void updateCustomer() {
        //Given
        long id = 10;
        Customer customer = new Customer(
                id, "foo", "testowy@gmail.com", 21
        );

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest(
                "Jakub", "jakub@gmail.com", 25
        );

        when(customerDao.existsPersonWithEmail("jakub@gmail.com")).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerCaptor.capture());

        Customer actual = customerCaptor.getValue();

        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());

    }

    @Test
    void updateCustomerName() {
        //Given
        String email = "testowy@gmail.com";
        long id = 1;
        Customer customer = new Customer(
                id,
                "Jakub",
                email,
                21
        );
        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest alex = new CustomerRequest("Alex", null, null);

        //When
        underTest.updateCustomer(id, alex);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(alex.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerNameAndEmail() {
        //Given
        long id = 1L;
        Customer customer = new Customer(
                id, "foo", "foo@gmail.com", 21
        );

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest("Jakub", "jakub@email.com", null);

        when(customerDao.existsPersonWithEmail("jakub@email.com")).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerCaptor.capture());

        Customer actual = customerCaptor.getValue();

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void willThrowExceptionWhenUpdateCustomerNotFound() {
        //Given
        long id = 10L;
        CustomerRequest request = new CustomerRequest("Jakub", "jakub@email.com", null);

        //When
        when(customerDao.findCustomerById(id)).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer id: %s not found".formatted(id));

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerEmailAlreadyExists() {
        //Given
        long id = 10L;
        String email = "foo@email.com";
        Customer customer = new Customer(
                id,
                "jakub",
                email,
                21
        );
        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest(
                "Jakub",
                "foo-test@email.com",
                21
        );


        //When
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(true);

        //Then
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(CustomerDuplicateException.class)
                .hasMessage("customer email %s already exists".formatted(request.email()));

        verify(customerDao, never()).updateCustomer(any());
    }
}