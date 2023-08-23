package com.codeday.productivity.service;

import com.codeday.productivity.exceptions.UserAlreadyExistsException;
import com.codeday.productivity.exceptions.UserNotFoundException;
import com.codeday.productivity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codeday.productivity.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class is a Spring Service component responsible for business logic
 * related to the User entity. It interacts with the UserRepository interface to perform
 * CRUD operations on User entities in the database.
 *
 * <p>
 * This class handles a variety of scenarios including creating a new user, updating
 * an existing user, retrieving a user by ID, and other relevant operations.
 * It also includes checks for exceptions like "User Already Exists" and "User Not Found."
 * </p>
 *
 * @author Nahom Alemu
 * @version 1.0
 * @see User
 * @see UserRepository
 */
@Service
public class UserService {

    private final UserRepository repository;

    /**
     * Constructs a UserService with the specified UserRepository.
     *
     * @param repository The UserRepository to use for CRUD operations.
     */
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a new user entity to the database.
     *
     * @param user The User entity to save.
     * @return The saved User entity.
     * @throws UserAlreadyExistsException If a user with the same email or ID already exists.
     */
    public User saveUser(User user) {
        Optional<User> existingUserByEmail = repository.findByEmail(user.getEmail());

        if (existingUserByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }

        Optional<User> existingUserById = repository.findById(user.getId());
        if (existingUserById.isPresent()) {
            throw new UserNotFoundException("User with ID " + user.getId() + " already exists.");
        }

        return repository.save(user);
    }

    /**
     * Saves a list of new user entities to the database.
     *
     * @param users The list of User entities to save.
     * @return The list of saved User entities.
     */
    public List<User> saveUsers(List<User> users) {
        // Your logic to check for duplicates, etc., could go here
        return repository.saveAll(users);
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of all User entities.
     */
    public List<User> getUsers() {
        return repository.findAll();
    }

    /**
     * Retrieves a user entity by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User entity with the specified ID.
     * @throws UserNotFoundException If the user with the specified ID does not exist.
     */
    public User getUserById(int id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " does not exist."));
    }

    /**
     * Retrieves users with the specified first name.
     *
     * @param firstName The first name to search for.
     * @return A list of users with the specified first name.
     */
    public List<User> getUsersByFirstName(String firstName) {
        return repository.findByFirstName(firstName);
    }

    /**
     * Retrieves users with the specified last name.
     *
     * @param lastName The last name to search for.
     * @return A list of users with the specified last name.
     */
    public List<User> getUsersByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    /**
     * Retrieves a user entity with the specified first and last name.
     *
     * @param firstName The first name to search for.
     * @param lastName The last name to search for.
     * @return The user entity that matches the specified first and last name.
     */
    public User getUserByFirstAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Updates an existing user entity.
     *
     * @param user The User entity with updated information.
     * @return The updated User entity.
     * @throws UserNotFoundException If the user with the specified ID does not exist.
     */
    public User updateUser(User user) {
        Optional<User> existingUser = repository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User with ID " + user.getId() + " does not exist.");
        }

        // Update existing user
        User updatedUser = existingUser.get();
        if (user.getFirstName() != null) {
            updatedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            updatedUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            updatedUser.setPassword(user.getPassword());
        }
        if (user.getIsActive() != null) {
            updatedUser.setIsActive(user.getIsActive());
        }

        return repository.save(updatedUser);
    }

    /**
     * Deactivates a user by setting its 'isActive' flag to "N".
     *
     * @param id The ID of the user to deactivate.
     * @return A message indicating that the user has been deactivated.
     * @throws UserNotFoundException If the user with the specified ID does not exist.
     */
    public String deactivateUser(int id) {
        Optional<User> existingUser = repository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User with ID " + id + " does not exist.");
        }

        User user = existingUser.get();
        user.setIsActive("N");
        repository.save(user);
        return "User deactivated || " + id;
    }
}
