package com.jazy.customer;

public record CustomerRequest(String name, String email, String password, Integer age, Gender gender) {}
