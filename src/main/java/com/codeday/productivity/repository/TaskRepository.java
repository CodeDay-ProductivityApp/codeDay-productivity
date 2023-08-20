package com.codeday.productivity.repository;

import com.codeday.productivity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.codeday.productivity.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //find by userId


    List<Task> findByUser(User user); // Finds all tasks by a given userId.

    @Query("SELECT t FROM Task t WHERE t.progress = ?1")
    List<Task> findByProgress(Double progress); // Finds all tasks by a given progress.

//    // Find tasks by goal through GoalTask
//    List<Task> findByGoalTasks_Goal_Id(Long goalId);
//
//    // Find tasks by reminder
//    List<Task> findByReminders_Id(Long reminderId);

    // Find tasks by title
    List<Task> findByTitleContaining(String title);

}


