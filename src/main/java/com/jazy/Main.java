package com.jazy;

import com.jazy.customer.Customer;
import com.jazy.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication

public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        //        printBean(applicationContext);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository repository) {
        return args -> {
            List<Customer> customers = List.of(
                    new Customer("Jakub", "jakub@mail.com", 22),
                    new Customer("Alex", "alex@mail.com", 21),
                    new Customer("Klaudia", "klaudia@mail.com", 24)
            );
            repository.saveAll(customers);
            System.out.println("Added Test Data");
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
