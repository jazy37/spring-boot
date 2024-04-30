package com.jazy.auth;

import com.jazy.customer.Gender;

public record SignUpRequest (String name, String email, String password, Integer age, Gender gender){}
