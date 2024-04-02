package com.jazy.customer;

import com.jazy.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        underTest.saveCustomer(customer);
        //WHEN
        List<Customer> customers = underTest.selectAllCustomers();
        //THEN
        assertThat(customers).isNotEmpty();
    }

    @Test
    void findCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE
        );
        underTest.saveCustomer(customer);

        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        Optional<Customer> actual = underTest.findCustomerById(id);

        //THEN
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void WillReturnEmptyWhenFindCustomerById() {
        //Given
        long id = -1;

        //When
        var actual = underTest.findCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void saveCustomer() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );

        //When
        underTest.saveCustomer(customer);

        Customer actual = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst().orElseThrow();

        //Then
        assertThat(actual).isNotNull();
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );

        underTest.saveCustomer(customer);

        //When
        boolean b = underTest.existsPersonWithEmail(customer.getEmail());

        //Then
        assertThat(b).isTrue();
    }

    @Test
    void existsPersonWithEmailNegative() {
        //Given
        String notExistsEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean b = underTest.existsPersonWithEmail(notExistsEmail);

        //Then
        assertThat(b).isFalse();
    }

    @Test
    void existsCustomerById() {
        //GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );

        underTest.saveCustomer(customer);

        long actualCustomerId = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        //WHEN
        boolean b = underTest.existsCustomerById(actualCustomerId);

        //THEN
        assertThat(b).isTrue();
    }

    @Test
    void deleteCustomer() {
        //GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );

        underTest.saveCustomer(customer);

        long actualCustomerId = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        //WHEN
        underTest.deleteCustomer(actualCustomerId);

        Optional<Customer> actual = underTest.findCustomerById(actualCustomerId);

        //THEN
        assertThat(actual).isEmpty();
    }

    @Test
    void updateCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE
        );

        underTest.saveCustomer(customer);

        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer customerToUpdate = underTest.findCustomerById(id).orElseThrow();
        customerToUpdate.setName(FAKER.name().fullName());
        customerToUpdate.setEmail(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID());
        customerToUpdate.setAge(80);

        //When
        underTest.updateCustomer(customerToUpdate);
        Optional<Customer> actual = underTest.findCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValue(customerToUpdate);
    }

    @Test
    void updateCustomerName() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE
        );

        underTest.saveCustomer(customer);

        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");

        //When
        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.findCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName()).isEqualTo("foo"); // change;
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}