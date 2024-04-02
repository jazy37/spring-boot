package com.jazy.auth;

import com.jazy.dto.CustomerDto;

public record AuthenticationResponse(String token, CustomerDto customerDto) {}
