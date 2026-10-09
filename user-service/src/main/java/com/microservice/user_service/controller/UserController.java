package com.microservice.user_service.controller;


import com.microservice.user_service.Exception.ResourceNotFoundException;
import com.microservice.user_service.module.User;
import com.microservice.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/Users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    //private static final Logger log = Logger.getLogger(UserController.class.getName());
	@Autowired
    UserService service;
	
	@GetMapping
	public List<User> getUserList()
	{
		return service.getAllUser();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id)
	{
		return service.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/create")
	public User createUser(@Valid @RequestBody User user)
	{

        log.debug("Inside createUser: " + user);
        return service.createUser(user);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<User> updateByUser(@PathVariable Long id,@Valid @RequestBody User user) throws ResourceNotFoundException
    {
		return ResponseEntity.ok(service.updateUser(id, user));
    }

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
