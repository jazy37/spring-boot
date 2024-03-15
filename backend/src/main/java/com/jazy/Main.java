package com.jazy;

import com.github.javafaker.Faker;
import com.jazy.customer.Customer;
import com.jazy.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@SpringBootApplication

public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        //        printBean(applicationContext);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository repository) {
        Faker faker = new Faker(new Locale("pl"));
        Random randomAge = new Random();
        return args -> {
            String name = faker.name().firstName();
            List<Customer> customers = List.of(
                new Customer(name, "%s@example.com".formatted(name), randomAge.nextInt(10,99), "Male")
            );
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
