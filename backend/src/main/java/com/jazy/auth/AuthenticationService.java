package com.jazy.auth;

import com.jazy.customer.Customer;
import com.jazy.dto.CustomerDTOMapper;
import com.jazy.dto.CustomerDto;
import com.jazy.jwt.JWTUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CustomerDTOMapper customerDTOMapper;



    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CustomerDTOMapper customerDTOMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerDTOMapper = customerDTOMapper;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Customer customer = (Customer) authentication.getPrincipal();
        CustomerDto customerDto = customerDTOMapper.apply(customer);
        String jwtToken = jwtUtil.issueToken(
                customerDto.username(), customerDto.roles());

        return new AuthenticationResponse(jwtToken, customerDto);
    }

    public ResponseCookie logout() {
        return jwtUtil.getCleanJwtCookie();
    }
}
