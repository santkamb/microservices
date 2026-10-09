package com.microservice.account.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class AccountRequestDto {

        private Long customerId;
        private String customerName;
        private String email;
        private String mobileNumber;
        private Long accountNumber;
        private String accountType;
        private String branchAddress;
        private Double balance;

}
