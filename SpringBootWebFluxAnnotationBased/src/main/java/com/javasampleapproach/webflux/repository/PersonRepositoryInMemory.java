package com.javasampleapproach.webflux.repository;

import com.javasampleapproach.webflux.model.Customer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
public class PersonRepositoryInMemory implements PersonRepository {

    private final Map<Long, Customer> customerStores = new HashMap<>();

    @PostConstruct
    public void initIt() throws Exception {
        customerStores.put(1L, new Customer(1, "Jack", "Smith", 20));
        customerStores.put(2L, new Customer(2, "Peter", "Johnson", 25));
        customerStores.put(3L, new Customer(3, "Sarah", "Johnson", 22));
    }

    @Override
    public Flux<Customer> getAll() {
        return Flux.fromIterable(customerStores.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(toList()));
    }

    @Override
    public Mono<Customer> getCustomer(Long id) {
        return Mono.justOrEmpty(customerStores.get(id));
    }

    @Override
    public void addCustomer(Customer customer) {
        customerStores.put(customer.getCustId(), customer);
    }

    @Override
    public void setCustomer(long id, Customer customer) {
        customerStores.put(id, customer);
    }

    @Override
    public void removeCustomer(Long id) {
        customerStores.remove(id);
    }
}
