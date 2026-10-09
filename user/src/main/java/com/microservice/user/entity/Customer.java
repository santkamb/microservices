package com.microservice.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Random;

@Entity
@Table(name="Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long customer_id;

    @NotBlank(message = "First name is required")
    private String first_name;
    @NotBlank(message = "Last name is required")
    private String last_name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digits")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must contain exactly 10 digits")
    private String mobileNumber;

    public Customer() {

    }

    public Customer(Long id, Long customer_id, String first_name, String last_name, String email, String mobileNumber) {
        this.id = id;
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }

    @PrePersist
    public void generateCustomerId() {
        if (customer_id == null) {
            customer_id = 10000L + new Random().nextInt(90000);
        }
    }

}

