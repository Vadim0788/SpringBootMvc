package com.dev.petlink.controller;

import com.dev.petlink.model.UserDto;
import com.dev.petlink.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserDto userToCreate
    ) {
        log.info("Get request for create user: user={}", userToCreate);
        var createdUser = userService.createUser(userToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserDto userToUpdate
    ) {
        log.info("Get request for put user by id: id={}, userToUpdate={}}", id, userToUpdate);
        return userService.updateUser(id, userToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request for delete user by id: id={}", id);
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public UserDto findById(
            @PathVariable("id") long id
    ) {
        log.info("Get request for find user by id: id={}", id);
        return userService.findById(id);
    }
}
