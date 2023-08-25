package com.codeday.productivity;

import com.codeday.productivity.controller.TaskController;
import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.Task;
import com.codeday.productivity.service.GoalService;
import com.codeday.productivity.service.TaskService;
import com.codeday.productivity.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)  // Replaces @RunWith with JUnit 5
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private GoalService goalService;

    @Test
    public void testCreateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Sample Task");
        Goal goal = new Goal();
        goal.setId(1);
        when(goalService.getGoalById(1)).thenReturn(goal);
        when(taskService.saveTaskForUserAndGoal(any(Goal.class), any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/v1/users/1/goals/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Task\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample Task")));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setId(1);

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/v1/users/1/goals/1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample Task")));
    }
}



