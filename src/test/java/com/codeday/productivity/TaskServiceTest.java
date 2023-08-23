package com.codeday.productivity;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.repository.TaskRepository;
import com.codeday.productivity.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Sample Task");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertEquals("Sample Task", createdTask.getTitle());
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertEquals(1L, foundTask.getId());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task foundTask = taskService.getTaskById(1L);

        assertNull(foundTask);
    }

    @Test
    void testGetAllTasksByGoal() {
        Task task1 = new Task();
        Task task2 = new Task();
        Goal goal = new Goal();

        when(taskRepository.findByGoal(goal)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasksByGoal(goal);

        assertEquals(2, tasks.size());
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Old Title");

        Task newTaskData = new Task();
        newTaskData.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(newTaskData);

        Task updatedTask = taskService.updateTask(1L, newTaskData);

        assertEquals("New Title", updatedTask.getTitle());
    }

    @Test
    void testUpdateTaskNotFound() {
        Task newTaskData = new Task();
        newTaskData.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task updatedTask = taskService.updateTask(1L, newTaskData);

        assertNull(updatedTask);
    }




}
