package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users/{userId}/goals")
public class GoalController {

    private static final Logger LOGGER = LogManager.getLogger(GoalController.class);

    private final GoalService goalService;
    private final UserService userService;


    // Constructor injection for the GoalService and UserService dependencies
    @Autowired
    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@PathVariable Integer userId, @RequestBody Goal goal) {
        LOGGER.info("Creating a Goal for User ID {}, getting User", userId);
        try {
            User user = userService.getUserById(userId);
            LOGGER.info("Setting the User to the Goal: {}", goal);
            goal.setUser(user);
            LOGGER.info("Saving the Goal to the Service");
            Goal _goal = goalService.saveGoal(goal);
            return new ResponseEntity<>(_goal, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoalsByUser(@PathVariable Integer userId) {
        LOGGER.info("Getting all Goal for User ID {}, getting User", userId);
        try {
            User user = userService.getUserById(userId);
            LOGGER.info("Returning all Goals for the User from the Service");
            List<Goal> goals = goalService.getAllGoalsByUser(user);
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/complete/{isComplete}")
    public ResponseEntity<List<Goal>> getAllGoalsByUserAndCompletion(
            @PathVariable Integer userId,
            @PathVariable String isComplete
    ) {
        LOGGER.info("Getting all Goal by Completion Status {} for User ID {}, getting User",
                isComplete, userId);
        try {
            User user = userService.getUserById(userId);
            LOGGER.info("Returning all Goals for the User from the Service");
            List<Goal> goals = goalService.getAllGoalsByUserAndCompletion(user, isComplete);
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date/{startDate}")
    public ResponseEntity<List<Goal>> getAllGoalsByUserAndStartDate(
            @PathVariable Integer userId, @PathVariable Instant startDate
    ) {
        LOGGER.info("Getting Goal with start date: {} and User ID: {}", startDate.toString(), userId);
        try {
            User user = userService.getUserById(userId);
            List<Goal> goals = goalService.getAllGoalsByUserAndStartDate(user, startDate);
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // returns null if id does not exist in the database
    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(@PathVariable Integer userId, @PathVariable Integer id) {
        LOGGER.info("Getting Goal with ID: {} and User ID: {}", id, userId);
        try {
            Goal goal = goalService.getGoal(id);
            if (!Objects.equals(goal.getUser().getId(), userId)) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(goal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // throws error status 404 if goal id does not exist in the database
    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoalById(
            @PathVariable Integer userId,
            @PathVariable Integer id,
            @RequestBody Goal goal
    ) {
        LOGGER.info("Updating Goal with ID: {}, User ID: {} with Goal data: {}", id, userId, goal);
        try {
            Goal _goal = goalService.getGoal(id);
            if (!Objects.equals(_goal.getUser().getId(), userId)) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Goal updatedGoal = goalService.updateGoal(id, goal);
            return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // returns null if id does not exist in the database
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Integer userId, @PathVariable Integer id) {
        LOGGER.info("Deleting Goal with ID: {}", id);
        try {
            Goal goal = goalService.getGoal(id);
            if (!Objects.equals(goal.getUser().getId(), userId)) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            goalService.deleteGoal(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/due", params = "timeFrame")
    public ResponseEntity<Iterable<Goal>> getUserGoalsDueBy(
            @PathVariable Integer userId, @RequestParam String timeFrame
    ) {
        LOGGER.info("Getting incomplete Goals for User with ID: {} for the following timeframe: {}",
                userId, timeFrame);
        try {
            User user = userService.getUserById(userId);
            String isComplete = "N";
            Iterable<Goal> goals = goalService.getUserGoalsDueBy(user, isComplete, timeFrame);
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
