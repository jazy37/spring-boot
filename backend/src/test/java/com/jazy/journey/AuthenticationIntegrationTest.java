package com.jazy.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.jazy.auth.AuthenticationRequest;
import com.jazy.customer.CustomerRequest;
import com.jazy.customer.Gender;
import com.jazy.dto.CustomerDto;
import com.jazy.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntegrationTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private WebTestClient webClient;
    private static final String AUTHENTICATION_URI = "api/v1/auth";
    private static final String CUSTOMER_URI = "/api/v1/customers";
    private static final Random RANDOM = new Random();

    @BeforeEach
    public void setUp() {
        webClient = webClient.mutate()
                .responseTimeout(Duration.ofMillis(30_000))
                .build();
    }

    @Test
    void canLoginCustomer() {

        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@email.com";
        String password = faker.internet().password();
        int age = RANDOM.nextInt(10, 80);

        CustomerRequest request =
                new CustomerRequest(name, email, password, age, Gender.MALE);

        AuthenticationRequest authenticationRequest =
                new AuthenticationRequest(email, password);

        //Given
        webClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //When
        EntityExchangeResult<CustomerDto> result = webClient.post()
                .uri(AUTHENTICATION_URI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDto>(){})
                .returnResult();

        ResponseCookie firstCookie = result.getResponseCookies().getFirst("accessToken");
        String token = "";
        if(firstCookie != null) {
            token = firstCookie.getValue();
        }
        CustomerDto customerDto = Objects.requireNonNull(result.getResponseBody());

        //Then
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }
}
