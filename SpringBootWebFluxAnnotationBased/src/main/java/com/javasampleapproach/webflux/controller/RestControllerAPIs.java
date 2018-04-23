package com.javasampleapproach.webflux.controller;

import com.javasampleapproach.webflux.model.Customer;
import com.javasampleapproach.webflux.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/customer")
@AllArgsConstructor
public class RestControllerAPIs {

    private final PersonRepository personRepository;

    @GetMapping("/")
    public Flux<Customer> getAll() {
        return personRepository.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer(@PathVariable Long id) {
        return personRepository.getCustomer(id);
    }

    @PostMapping("/post")
    public Mono<ResponseEntity<String>> postCustomer(@RequestBody Customer customer) {
        // do post
        personRepository.addCustomer(customer);

        // log on console
        System.out.println("########### POST:" + customer);

        return Mono.just(new ResponseEntity<>("Post Successfully!", HttpStatus.CREATED));
    }

    @PutMapping("/put/{id}")
    public Mono<ResponseEntity<Customer>> putCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        // reset customer.Id
        personRepository.setCustomer(id, customer.withCustId(id));

        // log on console
        System.out.println("########### PUT:" + customer);

        return Mono.just(new ResponseEntity<>(customer, HttpStatus.CREATED));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> deleteMethod(@PathVariable Long id) {
        // delete processing
        personRepository.removeCustomer(id);

        return Mono.just(new ResponseEntity<>("Delete Succesfully!", HttpStatus.ACCEPTED));
    }
}