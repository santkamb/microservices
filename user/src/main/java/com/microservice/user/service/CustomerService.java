package com.microservice.user.service;

import com.microservice.user.client.AccountFeignClient;
import com.microservice.user.dto.AccountRequestDto;
import com.microservice.user.dto.AccountResponseDto;
import com.microservice.user.dto.CustomerRequestDto;
import com.microservice.user.dto.CustomerResponseDTO;
import com.microservice.user.entity.Customer;
import com.microservice.user.exception.CustomerAlreadyException;
import com.microservice.user.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private static Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final AccountFeignClient  accountFeignClient;
    public CustomerService(CustomerRepository customerRepository, AccountFeignClient accountFeignClient) {
        this.customerRepository = customerRepository;
        this.accountFeignClient = accountFeignClient;
    }

    @CircuitBreaker(name = "account-service", fallbackMethod = "createAccountFallback")
    public CustomerResponseDTO createCustomer(CustomerRequestDto customerRequestDto) {
       Customer customer = new Customer();
       customer.setFirst_name(customerRequestDto.getFirst_name());
       customer.setLast_name(customerRequestDto.getLast_name());
       customer.setEmail(customerRequestDto.getEmail());
       customer.setMobileNumber(customerRequestDto.getMobileNumber());
        log.info("Creating customer: {}", customer.getCustomer_id());
        if (customerRepository.existsByEmail(customer.getEmail())) {
            try {
                throw new CustomerAlreadyException("Customer already exists with email: "
                        + customer.getEmail());
            } catch (CustomerAlreadyException e) {
                throw new RuntimeException(e);
            }
        }
        Customer savedCustomer =
                customerRepository.save(customer);

        CustomerResponseDTO response =
                new CustomerResponseDTO();
        response.setCustomer_id(savedCustomer.getCustomer_id());
        response.setFirst_name(savedCustomer.getFirst_name());
        response.setLast_name(savedCustomer.getLast_name());
        response.setEmail(savedCustomer.getEmail());
        response.setMobileNumber(savedCustomer.getMobileNumber());

        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setCustomerId(savedCustomer.getCustomer_id());
        accountRequestDto.setCustomerName(savedCustomer.getFirst_name()+" "+savedCustomer.getLast_name());
        accountRequestDto.setEmail(savedCustomer.getEmail());
        accountRequestDto.setMobileNumber(savedCustomer.getMobileNumber());

        //calling account service
        AccountResponseDto accountResponseDto = accountFeignClient.createAccount(accountRequestDto);
        log.info("Account created "+accountResponseDto);
        return response;
    }
     public CustomerResponseDTO  createAccountFallback(CustomerRequestDto customerRequestDto, Exception e) {
        log.error("Account Service is unavailable :{}" , e.getMessage());
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setFirst_name(customerRequestDto.getFirst_name());
        customerResponseDTO.setLast_name(customerRequestDto.getLast_name());
        customerResponseDTO.setEmail(customerRequestDto.getEmail());
        customerResponseDTO.setMobileNumber(customerRequestDto.getMobileNumber());
        return customerResponseDTO;


     }

     @CircuitBreaker(name="account-service",fallbackMethod = "getAccountDetailsFallback")
    public Optional<AccountResponseDto>getAccountDetails(Long id){
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto =accountFeignClient.getAccountDetails(id);
        return Optional.of(accountResponseDto);
    }

    public Optional<AccountResponseDto> getAccountDetailsFallback(Long id,Exception e1){
        log.error("Account service is unavailable :{}",e1.getMessage());
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountNumber(null);
         response.setAccountType("N/A");
        return Optional.of(response);
    }
}