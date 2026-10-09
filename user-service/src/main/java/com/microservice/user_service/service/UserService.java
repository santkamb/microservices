package com.microservice.user_service.service;


import com.microservice.user_service.Exception.ResourceNotFoundException;
import com.microservice.user_service.module.User;
import com.microservice.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
    UserRepository userRepos;

	public List<User> getAllUser() {
		return userRepos.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepos.findById(id);
	}

	public User createUser(User user) {
        logger.info("Creating user: " + user);
		return userRepos.save(user);
    }

	public User updateUser(Long id, User user) throws ResourceNotFoundException {
		//User userdetails = userRepos.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		//added custom excption
		User userdetails = userRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		userdetails.setCustFirstName(user.getCustFirstName());
		userdetails.setCustLastName(user.getCustLastName());
		userdetails.setEmail(user.getEmail());
		return userRepos.save(userdetails);

	}

	public void deleteUser(Long id) {
		userRepos.deleteById(id);
	}
}
