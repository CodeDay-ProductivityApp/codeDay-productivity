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
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Get a task by its ID
    public Task getTaskById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional.orElse(null);
    }

    //get tasks by goal
    public List<Task> getAllTasksByGoal(Goal goal) {
        return taskRepository.findByGoal(goal);
    }

    // save a task for a user and goal
    public Task saveTaskForUserAndGoal(Goal goal, Task task) {
        task.setGoal(goal);
        return taskRepository.save(task);
    }

    // Update a task by its ID
    public Task updateTask(Long taskId, Task taskData) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();

            existingTask.setTitle(taskData.getTitle());
            existingTask.setDescription(taskData.getDescription());

            return taskRepository.save(existingTask);
        }
        return null;
    }

    // Delete a task by its ID
    public boolean deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }
}

