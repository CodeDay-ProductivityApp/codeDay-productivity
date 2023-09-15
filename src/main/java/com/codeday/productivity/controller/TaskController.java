package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.TaskService;
import com.codeday.productivity.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;

@RestController
@RequestMapping("api/v1/users/{userId}/goals/{goalId}/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final UserService userService;
    private final GoalService goalService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, GoalService goalService) {
        this.taskService = taskService;
        this.userService = userService;
        this.goalService = goalService;
    }

    @PostMapping
    public Task createGoalTask( @PathVariable Integer goalId, @RequestBody Task task) {
        logger.info("Creating task for goal with id: " + goalId);
        Goal goal = goalService.getGoal(goalId);
        task.setGoal(goal);
        return taskService.saveTaskForUserAndGoal(goal, task);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> createTaskSubTask(
            @PathVariable Integer userId,
            @PathVariable Integer goalId,
            @PathVariable Integer id,
            @RequestBody Task task) {
        logger.info("Creating sub task for parent task with id: {}, ", id);
        try {
            logger.info("Getting task with id: {}, ", id);
            Task parentTask = taskService.getTaskById(id);
            logger.info("Getting goal with id: {}, ", goalId);
            Goal goal = goalService.getGoal(goalId);
            logger.info("Checking User Goal Task relationship");
            if ((parentTask == null)
                ||(!Objects.equals(goal.getUser().getId(), userId))
                || (!Objects.equals(goal.getId(), goalId))) {
                return ResponseEntity.notFound().build();
            }
            logger.info("Setting Task Goal to Goal ID: {}", goalId);
            task.setGoal(goal);
            task.setParentTask(parentTask);
            task.setIsSubTask("Y");
            logger.info("Save SubTask to Task ID: {}", id);
            Task subTask = taskService.createTask(task);
            return new ResponseEntity<>(subTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get task by id
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer taskId) {
        Task task = taskService.getTaskById(taskId);
        if(task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get task by goal id
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasksByGoal(@PathVariable int userId, @PathVariable int goalId) {
        User user = userService.getUserById(userId);
        Goal goal = goalService.getGoal(goalId);
        if (!Objects.equals(goal.getUser().getId(), user.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskService.getAllTasksByGoal(goal));
    }

    // Update task by id
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable Integer taskId, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        if(updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete task by id
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Integer taskId) {
        if(taskService.deleteTask(taskId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/test")
    public String testEndpoint() {
        return "Test works!";
    }

}


