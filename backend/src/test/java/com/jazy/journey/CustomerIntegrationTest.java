package com.jazy.journey;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.jazy.customer.CustomerRequest;
import com.jazy.customer.Gender;
import com.jazy.dto.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    private static final Random RANDOM = new Random();

    private static final String CUSTOMER_URI = "/api/v1/customers";

    @BeforeEach
    public void setUp() {
        webClient = webClient.mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @Test
    void canRegisterCustomer() {
        //create a CustomerRequest body
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@email.com";
        String password = faker.internet().password();
        int age = RANDOM.nextInt(10, 80);
        CustomerRequest request =
                new CustomerRequest(name, email, "password", age, Gender.MALE);

        //send post request
        String jwtToken = Objects.requireNonNull(webClient.post().uri(CUSTOMER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request), CustomerRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseCookies()
                        .getFirst("accessToken"))
                .getValue();

        //get all customers
        List<CustomerDto> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();

        assert allCustomers != null;
        var id = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        //make sure that customer created
        CustomerDto expectedCustomer = new CustomerDto(
                id, name, email, Gender.MALE, age, List.of("ROLE_USER"), email
        );
        assertThat(allCustomers).contains(expectedCustomer);

        //get Customer by id
        webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDto>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        //create a CustomerRequest body
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@email.com";
        int age = RANDOM.nextInt(10, 80);

        CustomerRequest request =
                new CustomerRequest(name, email, "password", age, Gender.MALE);

        CustomerRequest request1 =
                new CustomerRequest(name,email+"uk", "password", age ,Gender.MALE);

        //Send post request for customer 1
        webClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //send post request for customer 2
        String jwtToken = Objects.requireNonNull(webClient.post().uri(CUSTOMER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request1), CustomerRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseCookies()
                        .getFirst("accessToken"))
                .getValue();

        //get all customers
        List<CustomerDto> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer created

        assert allCustomers != null;
        //get ID for customer 1
        var id = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        //customer 2 delete customer 1
        webClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk();

        //Customer 2 get Customer1 ByID
        webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        //create a CustomerRequest expected
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@email.com";
        int age = RANDOM.nextInt(10, 80);
        CustomerRequest request =
                new CustomerRequest(name, email, "password", age, Gender.MALE);

        //send post request
        String jwtToken = Objects.requireNonNull(webClient.post().uri(CUSTOMER_URI)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request), CustomerRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseCookies()
                        .getFirst("accessToken"))
                .getValue();

        //get all customers
        List<CustomerDto> allCustomers = webClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();



        assert allCustomers != null;
        //make sure that customer created and getID
        var id = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        String newName = "newName";
        CustomerRequest requestToUpdate =
                new CustomerRequest(newName, null, "password", null, null);


        webClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestToUpdate), CustomerRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get Customer by id
        CustomerDto updated = webClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .cookie("accessToken", jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDto.class)
                .returnResult()
                .getResponseBody();

        CustomerDto expected = new CustomerDto(
                id, newName, email, Gender.MALE, age, List.of("ROLE_USER"), email
        );
        assert updated != null;
        assertThat(expected).isEqualTo(updated);
    }
}
