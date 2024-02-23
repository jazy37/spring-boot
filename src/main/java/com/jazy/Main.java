package com.jazy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class Main {
    public static void main(String[] args) {


        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);


//        printBean(applicationContext);
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
