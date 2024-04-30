package com.jazy.customer;

import java.util.Set;

public record CustomerRequest(String name, String email, String password, Integer age, Gender gender) {}
