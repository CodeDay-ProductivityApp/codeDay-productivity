package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    public Goal createGoal(@PathVariable int userId, @RequestBody Goal goal) {
        LOGGER.info("Creating a Goal for User ID {}, getting User", userId);
        User user = userService.getUserById(userId);
        LOGGER.info("Setting the User to the Goal: {}", goal);
        goal.setUser(user);
        LOGGER.info("Saving the Goal to the Service");
        return goalService.saveGoal(goal);
    }

    @GetMapping
    public List<Goal> getAllGoalsByUser(@PathVariable int userId) {
        LOGGER.info("Getting all Goal for User ID {}, getting User", userId);
        User user = userService.getUserById(userId);
        LOGGER.info("Returning all Goals for the User from the Service");
        return goalService.getAllGoalsByUser(user);
    }

    @GetMapping("/complete/{isComplete}")
    public List<Goal> getAllGoalsByUserAndCompletion(@PathVariable int userId, @PathVariable String isComplete) {
        LOGGER.info("Getting all Goal by Completion Status {} for User ID {}, getting User",
                isComplete, userId);
        User user = userService.getUserById(userId);
        LOGGER.info("Returning all Goals for the User from the Service");
        return goalService.getAllGoalsByUserAndCompletion(user, isComplete);
    }

    @GetMapping("/date/{startDate}")
    public List<Goal> getAllGoalsByUserAndStartDate(@PathVariable int userId, @PathVariable Instant startDate){
        User user = userService.getUserById(userId);
        return goalService.getAllGoalsByUserAndStartDate(user, startDate);
    }

    // returns null if id does not exist in the database
    @GetMapping("/{id}")
    public Goal getGoalById(@PathVariable int id) {
        LOGGER.info("Getting Goal with ID: {}", id);
        return goalService.getGoal(id);
    }

    // throws error status 404 if goal id does not exist in the database
    @PutMapping("/{id}")
    public Optional<Goal> updateGoalById(@PathVariable int id, @RequestBody Goal goal) {
        LOGGER.info("Updating Goal with ID: {} with Goal data: {}", id, goal);
        return Optional.ofNullable(goalService.updateGoal(id, goal));
    }

    // returns null if id does not exist in the database
    @DeleteMapping("{id}")
    public Goal deleteGoal(@PathVariable int id) {
        LOGGER.info("Deleting Goal with ID: {}", id);
        Goal goal = goalService.getGoal(id);
        goalService.deleteGoal(id);
        return goal;
    }

    @GetMapping(value = "/due", params = "timeFrame")
    public Iterable<Goal> getUserGoalsDueBy(@PathVariable int userId, @RequestParam String timeFrame) {
        LOGGER.info("Getting incomplete Goals for User with ID: {} for the following timeframe: {}",
                userId, timeFrame);
        User user = userService.getUserById(userId);
        String isComplete = "N";
        return goalService.getUserGoalsDueBy(user, isComplete, timeFrame);
    }
}
