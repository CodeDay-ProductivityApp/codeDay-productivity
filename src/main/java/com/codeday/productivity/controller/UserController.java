package com.codeday.productivity.controller;

import com.codeday.productivity.entity.User;
import com.codeday.productivity.exceptions.UserAlreadyExistsException;
import com.codeday.productivity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.codeday.productivity.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserController provides RESTful API endpoints for managing users.
 * The class handles CRUD operations related to {@link User} entities and
 * employs {@link UserService} for the underlying business logic.
 *
 * <p>
 * It allows adding a new user, fetching users based on different criteria,
 * updating user information, and deactivating users.
 * </p>
 *
 * @author Nahom Alemu
 * @version 1.0
 */
@RestController
@RequestMapping("api/v1")
public class UserController {

    private final UserService service;

    /**
     * Initializes a new instance of {@code UserController}.
     *
     * @param service The UserService instance for handling business logic.
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Adds a new user to the system.
     *
     * @param user The user to add.
     * @return The added user.
     */
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return service.saveUser(user);
    }

    /**
     * Adds a new {@link User} to the database.
     *
     * @param users The {@link User} entity to be added.
     * @return The {@link User} entity that was added.
     */
    @PostMapping("/users/batch")
    public List<User> addUsers(@RequestBody List<User> users){
        return service.saveUsers(users);
    }

    /**
     * Retrieves {@link User} entities based on provided first and/or last name.
     * If no criteria are provided, returns all users.
     *
     * @param firstName The first name to search for (optional).
     * @param lastName The last name to search for (optional).
     * @return A {@link ResponseEntity} containing either the users that match the criteria or an error message.
     */

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName
    ) {
        List<User> users = new ArrayList<>();

        if (firstName != null && lastName != null) {
            User user = service.getUserByFirstAndLastName(firstName, lastName);
            if (user != null) {
                return new ResponseEntity<>(List.of(user), HttpStatus.OK);
            }
        } else if (firstName != null) {
            users = service.getUsersByFirstName(firstName);
        } else if (lastName != null) {
            users = service.getUsersByLastName(lastName);
        } else {
            return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
        }

        if (users.isEmpty()) {
            return new ResponseEntity<>("User doesn't exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    /**
     * Retrieves a {@link User} entity by its ID.
     *
     * @param id The ID of the user.
     * @return The {@link User} entity with the specified ID.
     */
    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) {
        return service.getUserById(id);
    }

    /**
     * Updates an existing {@link User} entity.
     *
     * @param id The ID of the user to be updated.
     * @param user The new {@link User} details.
     * @return The updated {@link User} entity.
     */
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        return service.updateUser(user);
    }

    /**
     * Deactivates a {@link User} entity by setting its 'isActive' field to 'N'.
     *
     * @param id The ID of the user to be deactivated.
     * @return A message confirming the deactivation.
     */
    @PutMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable int id) {
        return service.deactivateUser(id);
    }

    /**
     * Exception handler for {@link UserNotFoundException}.
     *
     * @param ex The caught exception.
     * @return A {@link ResponseEntity} with a 404 status and the exception's message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Exception handler for {@link UserAlreadyExistsException}.
     *
     * @param ex The caught exception.
     * @return A {@link ResponseEntity} with a 409 status and the exception's message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

