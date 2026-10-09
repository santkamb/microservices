package com.microservice.user.controller;

import com.microservice.user.dto.AccountResponseDto;
import com.microservice.user.dto.CustomerRequestDto;
import com.microservice.user.dto.CustomerResponseDTO;
import com.microservice.user.entity.Customer;
import com.microservice.user.exception.CustomerAlreadyException;
import com.microservice.user.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private static Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDto customer) {
        log.debug("Inside createCustomer: " + customer.getEmail());
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customer);
        log.info("customer created Successfully :"+createdCustomer.getCustomer_id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);

    }

    @GetMapping("/{id}")
    ResponseEntity<AccountResponseDto> getAccountDetails(@PathVariable Long id) {
        Optional<AccountResponseDto> accountDetails = customerService.getAccountDetails(id);
        return accountDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
