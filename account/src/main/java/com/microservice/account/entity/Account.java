package com.microservice.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Account extends BaseEntity {
    @Column(name="customer_id")
    private Long customerId;

    @Column(name="customer_Name")
    private String customerName;

    @Column(name="customer_email")
    private String email;

    @Column(name="customer_mobile")
    private String mobile;

    @Column(name="account_number")
    @Id
    private Long accountNumber;

    @Column(name="account_type")
    private String accountType;

    @Column(name="branch_address")
    private String branchAddress;

    @Column(name="balance")
    private double balance;


    @PrePersist
    public void generateAccountNumber() {
        if (accountNumber == null) {
            accountNumber = ThreadLocalRandom.current()
                    .nextLong(100000000000000L, 1000000000000000L);
        }
    }
}
