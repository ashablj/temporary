package com.javasampleapproach.webflux.repository;

import com.javasampleapproach.webflux.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
    Flux<Customer> getAll();

    Mono<Customer> getCustomer(Long id);

    void addCustomer(Customer customer);

    void setCustomer(long id, Customer customer);

    void removeCustomer(Long id);
}
