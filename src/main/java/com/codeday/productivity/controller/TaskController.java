package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.TaskService;
import com.codeday.productivity.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/users/{userId}/goals/{goalId}/tasks")
public class TaskController {
    private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

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
    public ResponseEntity<Task> createGoalTask(
            @PathVariable Integer userId, @PathVariable Integer goalId, @RequestBody Task task
    ) {
        LOGGER.info("Creating task for goal with id: " + goalId);
        try {
            User user = userService.getUserById(userId);
            Goal goal = goalService.getGoal(goalId);
            if (!Objects.equals(goal.getUser().getId(), userId)) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            task.setUser(user);
            task.setGoal(goal);
            Task _task = taskService.saveTaskForUserAndGoal(goal, task);
            return new ResponseEntity<>(_task, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> createTaskSubTask(
            @PathVariable Integer userId,
            @PathVariable Integer goalId,
            @PathVariable Integer id,
            @RequestBody Task task) {
        LOGGER.info("Creating sub task for parent task with id: {}, ", id);
        try {
            LOGGER.info("Getting task with id: {} goal id {}, user id: {}", id, goalId, userId);
            Task parentTask = taskService.getTaskById(id);
            Goal goal = goalService.getGoal(goalId);
            User user = userService.getUserById(userId);
            LOGGER.info("Checking User Goal Task relationship");
            if ((parentTask == null)
                ||(!Objects.equals(goal.getUser().getId(), userId))
                || (!Objects.equals(goal.getId(), goalId))) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            LOGGER.info("Setting Sub Task User to User ID: {}", userId);
            task.setUser(user);
            LOGGER.info("Setting Sub Task Goal to Goal ID: {}", goalId);
            task.setGoal(goal);
            LOGGER.info("Setting Sub Task Parent Task to Task ID: {}", id);
            task.setParentTask(parentTask);
            task.setIsSubTask("Y");
            LOGGER.info("Saving Task to repo via service");
            Task subTask = taskService.createTask(task);
            return new ResponseEntity<>(subTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get task by id
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Integer userId,
            @PathVariable Integer goalId,
            @PathVariable Integer taskId
    ) {
        LOGGER.info("Getting Task with id: {}, User ID: {}, and Goal ID: {}", taskId, userId, goalId);
        try {
            Goal goal = goalService.getGoal(goalId);
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            if (!Objects.equals(userId, goal.getUser().getId())
                    || (!Objects.equals(userId, task.getUser().getId()))) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(task, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get task by goal id
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasksByGoal(@PathVariable Integer userId, @PathVariable Integer goalId) {
        LOGGER.info("Getting all Tasks with User ID: {}, and Goal ID: {}", userId, goalId);
        try {
            Goal goal = goalService.getGoal(goalId);
            if (!Objects.equals(goal.getUser().getId(), userId)) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            List<Task> tasks = taskService.getAllTasksByGoal(goal);
            return new ResponseEntity<>(tasks, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update task by id
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTaskById(
            @PathVariable Integer userId,
            @PathVariable Integer goalId,
            @PathVariable Integer taskId,
            @RequestBody Task task
    ) {
        LOGGER.info("Updating Task with ID: {}, User ID: {}, and Goal ID: {}", taskId, userId, goalId);
        try {
            Task _task = taskService.getTaskById(taskId);
            if (!Objects.equals(_task.getUser().getId(), userId)
                || (!Objects.equals(_task.getGoal().getId(), goalId))
            ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Task updatedTask = taskService.updateTask(taskId, task);
            return new ResponseEntity<>(updatedTask, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete task by id
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(
            @PathVariable Integer userId,
            @PathVariable Integer goalId,
            @PathVariable Integer taskId
    ) {
        LOGGER.info("Deleting Task with ID: {}, User ID: {}, and Goal ID: {}", taskId, userId, goalId);
        try {
            Task task = taskService.getTaskById(taskId);
            if (!Objects.equals(task.getGoal().getId(), goalId) || (!Objects.equals(task.getUser().getId(), userId))) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
                taskService.deleteTask(taskId);
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
