package com.jazy;

import com.github.javafaker.Faker;
import com.jazy.customer.Customer;
import com.jazy.customer.ERole;
import com.jazy.customer.Gender;
import com.jazy.customer.Role;
import com.jazy.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication

public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        //        printBean(applicationContext);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository repository, PasswordEncoder passwordEncoder) {
        Faker faker = new Faker(new Locale("pl"));
        Random randomAge = new Random();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(ERole.ROLE_USER));
        return args -> {
            String name = faker.name().firstName();
            List<Customer> customers = List.of(
                new Customer(name, "%s@example.com".formatted(name),
                        passwordEncoder.encode(UUID.randomUUID().toString()),
                        randomAge.nextInt(10,99),
                        Gender.MALE));
//            repository.saveAll(customers);
        };
    }
    @Bean
    public Foo getFoo() {
        return new Foo("bar");
    }
    record Foo(String name) {}
    private static void printBean(ConfigurableApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
}
