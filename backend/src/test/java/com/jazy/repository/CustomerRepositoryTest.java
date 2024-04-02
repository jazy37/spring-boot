package com.jazy.repository;

import com.jazy.AbstractTestcontainers;
import com.jazy.TestConfig;
import com.jazy.customer.Customer;
import com.jazy.customer.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        underTest.save(customer);

        String email = underTest.findAll().stream()
                .map(Customer::getEmail)
                .filter(cEmail -> cEmail.equals(customer.getEmail()))
                .findFirst()
                .orElseThrow();

        //When
        boolean b = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(b).isTrue();
    }

    @Test
    void existsCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        underTest.save(customer);

        long id = underTest.findAll().stream()
                .map(Customer::getId)
                .filter(cId -> Objects.equals(cId, customer.getId()))
                .findFirst()
                .orElseThrow();

        //When
        boolean b = underTest.existsCustomerById(id);

        //Then
        assertThat(b).isTrue();
    }
}