package com.microservice.account.repository;

import com.microservice.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByEmail(String email);
    Optional<Account> findByCustomerId(Long customerId);
}
