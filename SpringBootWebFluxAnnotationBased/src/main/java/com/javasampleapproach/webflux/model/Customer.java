package com.javasampleapproach.webflux.model;

import lombok.Value;
import lombok.experimental.Wither;

@Value
public class Customer {
    @Wither
    long custId;
    String firstname;
    String lastname;
    int age;
}