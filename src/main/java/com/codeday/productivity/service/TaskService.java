package com.codeday.productivity.service;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Retrieve all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create a new task
    public Task createTask(Task task) {return taskRepository.save(task); }

    // Get a task by its ID
    public Task getTaskById(Integer taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional.orElse(null);
    }

    //get tasks by goal
    public List<Task> getAllTasksByGoal(Goal goal) {
        return taskRepository.findByGoal(goal);
    }

    // Save a task for a user and goal
    public Task saveTaskForUserAndGoal(Goal goal, Task task) {
        if (goal == null || task == null) {
            throw new IllegalArgumentException("Neither goal nor task can be null");
        }

        if (task.getId() != null && taskRepository.existsById(task.getId())) {
            // The task exists, so we'll update it.
            return updateTask(task.getId(), task);
        }

        // If the task doesn't exist, save it as a new task.
        task.setGoal(goal);
        return taskRepository.save(task);
    }

    // Update a task by its ID
    public Task updateTask(Integer taskId, Task taskData) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();

            if(taskData.getIsComplete() == null) {
                taskData.setIsComplete("N");
            }
            // Update non-null fields
            if (taskData.getTitle() != null) {
                existingTask.setTitle(taskData.getTitle());
            }
            if (taskData.getDescription() != null) {
                existingTask.setDescription(taskData.getDescription());
            }
            if (taskData.getStartDate() != null) {
                existingTask.setStartDate(taskData.getStartDate());
            }
            if (taskData.getEndDate() != null) {
                existingTask.setEndDate(taskData.getEndDate());
            }
            if (taskData.getIsComplete() != null) {
                existingTask.setIsComplete(taskData.getIsComplete());
            }
            if (taskData.getProgress() != null) {
                existingTask.setProgress(taskData.getProgress());
            }
            if (taskData.getLastUpdated() != null) {
                existingTask.setLastUpdated(taskData.getLastUpdated());
            }
            if (taskData.getTimeSpent() != null) {
                existingTask.setTimeSpent(taskData.getTimeSpent());
            }
            return taskRepository.save(existingTask);
        }
        return null;
    }

    // Delete a task by its ID
    public boolean deleteTask(Integer taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);


            return true;
        }
        return false;
    }
}

