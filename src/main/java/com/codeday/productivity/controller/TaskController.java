package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users/{userId}/goals/{goalId}/tasks")
public class TaskController {

    // IntelliJ asked for this change, it was a redundant cast
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final GoalService goalService;

    @Autowired
    public TaskController(TaskService taskService, GoalService goalService) {
        this.taskService = taskService;
        this.goalService = goalService;
    }

    // The mapping and bidirectional relationship knows that when you set the Goal to the Task
    // The Goal is updated to include the Task as well, it is how I have the GoalController for a UserGoal
    @PostMapping
    public Task createTask( @PathVariable Integer goalId, @RequestBody Task task) {
        logger.info("Creating task for goal with id: " + goalId);
        Goal goal = goalService.getGoal(goalId);
        task.setGoal(goal);
        return taskService.createTask(task);
    }



    // Get task by id
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        if(task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get task by goal id
    // I took out the user information as this is for Tasks assigned to Goals, you just need a Goal ID
    @GetMapping
    public List<Task> getAllTasksByGoal(@PathVariable int goalId) {
        Goal goal = goalService.getGoal(goalId);
        return taskService.getAllTasksByGoal(goal);
    }

    // Update task by id
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTaskById(@PathVariable Long taskId, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        if(updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete task by id
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long taskId) {
        if(taskService.deleteTask(taskId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // I don't think we need to verify this, as when we create a Goal it has a User
    private void verifyUserGoalAssociation(User user, Goal goal) {
        if (goal.getUser().getId() != user.getId()) {
            throw new RuntimeException("Goal does not belong to the given user");
        }
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test works!";
    }

}


