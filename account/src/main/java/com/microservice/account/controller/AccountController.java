package com.microservice.account.controller;

import com.microservice.account.dto.AccountRequestDto;
import com.microservice.account.dto.AccountResponseDto;
import com.microservice.account.entity.Account;
import com.microservice.account.exception.AccountAlreadyException;
import com.microservice.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid  @RequestBody AccountRequestDto accountServiceDto) throws AccountAlreadyException {
        AccountResponseDto responseDto=accountService.createAccount(accountServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable Long id)  {
        Optional<Account> account = accountService.findByAccountDetails(id);
        Account accountResp = account.get();
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountNumber(accountResp.getAccountNumber());
        response.setBalance(accountResp.getBalance());
        response.setCustomerId(accountResp.getCustomerId());
        response.setCustomerName(accountResp.getCustomerName());
        response.setAccountType(accountResp.getAccountType());
        response.setBranchAddress(accountResp.getBranchAddress());
        response.setEmail(accountResp.getEmail());
        response.setMobileNumber(accountResp.getMobile());
        return ResponseEntity.ok(response);

    }

}
