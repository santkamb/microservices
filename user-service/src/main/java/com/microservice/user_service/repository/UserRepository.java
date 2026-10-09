package com.microservice.user_service.repository;


import com.microservice.user_service.module.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
