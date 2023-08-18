package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/{userId}/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;


    // Constructor injection for the GoalService and UserService dependencies
    @Autowired
    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    @PostMapping
    public Goal createGoal(@PathVariable int userId, @RequestBody Goal goal){
        User user = userService.getUserById(userId);
        goal.setUser(user);
        return goalService.saveGoal(goal);
    }

    @GetMapping
    public List<Goal> getAllGoalsByUser(@PathVariable int userId){
        User user = userService.getUserById(userId);
        return goalService.getAllGoalsByUser(user);
    }

    @GetMapping("/complete/{isComplete}")
    public List<Goal> getAllGoalsByUserAndCompletion(@PathVariable int userId, @PathVariable String isComplete){
        User user = userService.getUserById(userId);
        return goalService.getAllGoalsByUserAndCompletion(user, isComplete);
    }

    @GetMapping("/date/{startDate}")
    public List<Goal> getAllGoalsByUserAndStartDate(@PathVariable int userId, @PathVariable Instant startDate){
        User user = userService.getUserById(userId);
        return goalService.getAllGoalsByUserAndStartDate(user, startDate);
    }

    // returns null if id does not exist in the database
    @GetMapping("/{id}")
    public Optional<Goal> getGoalById(@PathVariable int id) {
        return goalService.getGoal(id);
    }

    // throws error status 404 if goal id does not exist in the database
    @PutMapping("/{id}")
    public Optional<Goal> updateGoalById(@PathVariable int id, @RequestBody Goal goal) {
        return Optional.ofNullable(goalService.updateGoal(id, goal));
    }

    // returns null if id does not exist in the database
    @DeleteMapping("{id}")
    public Optional<Goal> deleteGoal(@PathVariable int id) {
        Optional<Goal> goal = goalService.getGoal(id);
        goalService.deleteGoal(id);
        return goal;
    }
}
