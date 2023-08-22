package com.codeday.productivity.repository;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.codeday.productivity.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Finds all tasks by a given progress.
    @Query("SELECT t FROM Task t WHERE t.progress = ?1")
    List<Task> findByProgress(Double progress);

    // Finds all tasks by a given goal.
    List<Task> findByGoal(Goal goal);

    // Find tasks by title
    List<Task> findByTitleContaining(String title);

}


