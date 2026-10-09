package com.microservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CustomerResponseDTO {
    private Long customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private String mobileNumber;


}
