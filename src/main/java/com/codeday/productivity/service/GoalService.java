package com.codeday.productivity.service;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    private final GoalRepository repository;

    @Autowired  // Constructor injection
    public GoalService(GoalRepository repository) {
        this.repository = repository;
    }

    public Goal saveGoal(Goal goal){
        return repository.save(goal);
    }

    public List<Goal> getAllGoalsByUser(User user){
        return repository.findByUser(user);
    }

    public List<Goal> getAllGoalsByUserAndCompletion(User user, String isComplete){
        return repository.findByUserAndIsComplete(user, isComplete);
    }

    public List<Goal> getAllGoalsByUserAndStartDate(User user, Instant startDate){
        return repository.findByUserAndStartDate(user, startDate);
    }

    public Optional<Goal> getGoal(int id) {
        return repository.findById(id);
    }

    public Goal updateGoal(int id, Goal goal) {
        Goal _goal = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found."));
        if (goal.getTitle() != null) {
            _goal.setTitle(goal.getTitle());
        }
        if (goal.getDescription() != null) {
            _goal.setDescription(goal.getDescription());
        }
        if (goal.getStartDate() != null) {
            _goal.setStartDate(goal.getStartDate());
        }
        if (goal.getDueDate() != null) {
            _goal.setDueDate(goal.getDueDate());
        }
        if (goal.getEndDate() != null) {
            _goal.setEndDate(goal.getEndDate());
        }
        if (goal.getProgress() != null) {
            _goal.setProgress(goal.getProgress());
        }
        if (goal.getIsComplete() != null) {
            _goal.setIsComplete(goal.getIsComplete());
        }
        return repository.save(_goal);
    }

    public void deleteGoal(int id) {
        repository.deleteById(id);
    }
}
