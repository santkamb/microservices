package com.microservice.user.client;

import com.microservice.user.dto.AccountResponseDto;
import com.microservice.user.dto.AccountRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="account-service")
public interface AccountFeignClient {
   @GetMapping("/api/v1/accounts/{id}")
    public AccountResponseDto getAccountDetails(@PathVariable("id") Long id);

    @PostMapping("/api/v1/accounts/create")
    public AccountResponseDto createAccount(@RequestBody AccountRequestDto accountServiceDto);
}
