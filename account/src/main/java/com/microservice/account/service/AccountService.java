package com.microservice.account.service;

import com.microservice.account.dto.AccountRequestDto;
import com.microservice.account.dto.AccountResponseDto;
import com.microservice.account.entity.Account;
import com.microservice.account.exception.AccountAlreadyException;
import com.microservice.account.exception.AccountNotFoundException;
import com.microservice.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) throws AccountAlreadyException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(accountRequestDto.getEmail());
        if (optionalAccount.isPresent()) {
           throw new AccountAlreadyException("Account Already Created with email" + accountRequestDto.getEmail());
        }
        Account account = new Account();
         account.setCustomerId(accountRequestDto.getCustomerId());
         account.setCustomerName(accountRequestDto.getCustomerName());
         account.setEmail(accountRequestDto.getEmail());
         account.setMobile(accountRequestDto.getMobileNumber());
         if(accountRequestDto.getAccountType()==null)
         {
             account.setAccountType("Saving");
         }else
         {
             account.setAccountType(accountRequestDto.getAccountType());
         }
        if(accountRequestDto.getBranchAddress()==null)
        {
            account.setBranchAddress("AT SHELDARA POST CHERA TALUKA JALKOT");
        }
        else
        {
            account.setBranchAddress(accountRequestDto.getBranchAddress());
        }
        if(accountRequestDto.getBalance()==null)
        {
            account.setBalance(25000);
        }
        else
        {
            account.setBalance(accountRequestDto.getBalance());
        }

         Account savedAccount = accountRepository.save(account);

         AccountResponseDto accountResponseDto = new AccountResponseDto();
          accountResponseDto.setCustomerId(savedAccount.getCustomerId());
          accountResponseDto.setAccountNumber(savedAccount.getAccountNumber());
          accountResponseDto.setCustomerName(savedAccount.getCustomerName());
          accountResponseDto.setEmail(savedAccount.getEmail());
          accountResponseDto.setMobileNumber(savedAccount.getMobile());
          accountResponseDto.setAccountType(savedAccount.getAccountType());
          accountResponseDto.setBalance(savedAccount.getBalance());
          accountResponseDto.setBranchAddress(savedAccount.getBranchAddress());
          return accountResponseDto;

    }

   public  Optional<Account> findByAccountDetails(Long customerId)  {
        return Optional.ofNullable(accountRepository.findByCustomerId(customerId).orElseThrow(() ->
                new AccountNotFoundException(
                        "Account not found for customer id: " + customerId)));
    }
}
