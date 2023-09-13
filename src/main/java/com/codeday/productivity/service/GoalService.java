package com.codeday.productivity.service;

import com.codeday.productivity.entity.Goal;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.repository.GoalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class GoalService {

    private static final Logger LOGGER = LogManager.getLogger(GoalService.class);

    private final GoalRepository repository;

    @Autowired  // Constructor injection
    public GoalService(GoalRepository repository) {
        this.repository = repository;
    }

    public Goal saveGoal(Goal goal){
        LOGGER.info("Saving Goal: {} to the Repo", goal);
        return repository.save(goal);
    }

    public List<Goal> getAllGoalsByUser(User user){
        LOGGER.info("Finding all Goals for User ID: {}", user.getId());
        return repository.findByUser(user);
    }

    public List<Goal> getAllGoalsByUserAndCompletion(User user, String isComplete){
        LOGGER.info("Getting Goals for User ID: {} marked by isComplete = {}", user.getId(), isComplete);
        return repository.findByUserAndIsComplete(user, isComplete);
    }

    public List<Goal> getAllGoalsByUserAndStartDate(User user, Instant startDate){
        return repository.findByUserAndStartDate(user, startDate);
    }

    public Goal getGoal(int id) {
        LOGGER.info("Getting Goal with ID: {}", id);
        Optional<Goal> goal = repository.findById(id);
        return goal.orElse(null);
    }

    public Goal updateGoal(int id, Goal goal) {
        LOGGER.info("Getting Goal with ID: {} to Update with Goal: {}", id, goal);
        Goal _goal = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found."));
        if (goal.getTitle() != null) {
            LOGGER.info("Updating Goal Title");
            _goal.setTitle(goal.getTitle());
        }
        if (goal.getDescription() != null) {
            LOGGER.info("Updating Goal Description");
            _goal.setDescription(goal.getDescription());
        }
        if (goal.getStartDate() != null) {
            LOGGER.info("Updating Goal Start Date");
            _goal.setStartDate(goal.getStartDate());
        }
        if (goal.getDueDate() != null) {
            LOGGER.info("Updating Goal Due Date");
            _goal.setDueDate(goal.getDueDate());
        }
        if (goal.getEndDate() != null) {
            LOGGER.info("Updating Goal End Date");
            _goal.setEndDate(goal.getEndDate());
        }
        if (goal.getProgress() != null) {
            LOGGER.info("Updating Goal Progress");
            _goal.setProgress(goal.getProgress());
        }
        if (goal.getIsComplete() != null) {
            LOGGER.info("Updating Goal Completion");
            _goal.setIsComplete(goal.getIsComplete());
        }
        LOGGER.info("Saving Goal to Repo");
        return repository.save(_goal);
    }

    public void deleteGoal(int id) {
        LOGGER.info("Deleting Goal with ID: {} from Repo", id);
        repository.deleteById(id);
    }

    public Iterable<Goal> getUserGoalsDueBy(User user, String isComplete, String timeFrame) {
        LOGGER.info("Getting Goals for User ID: {} marked by isComplete = {}", user.getId(), isComplete);
        List<Goal> _goals = repository.findByUserAndIsComplete(user, isComplete);
        LOGGER.info("Computing timeframe {} for Goals Due By", timeFrame);
        ZonedDateTime _dueBefore = Instant.now().atZone(ZoneId.of("UTC"));
        switch (timeFrame) {
            case "today" -> {
                _dueBefore = _dueBefore.plusDays(1);
                LOGGER.info("Timeframe: {} is computed for Goals Due By TODAY {}", timeFrame, _dueBefore);
            }
            case "week" -> {
                _dueBefore = _dueBefore.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
                LOGGER.info("Timeframe: {} is computed for Goals Due By WEEK {}", timeFrame, _dueBefore);
            }
            case "month" -> {
                _dueBefore = _dueBefore.with(TemporalAdjusters.firstDayOfNextMonth());
                LOGGER.info("Timeframe: {} is computed for Goals Due By MONTH {}", timeFrame, _dueBefore);
            }
            case "year" -> {
                _dueBefore = _dueBefore.with(TemporalAdjusters.firstDayOfNextYear());
                LOGGER.info("Timeframe: {} is computed for Goals Due By YEAR {}", timeFrame, _dueBefore);
            }
        }
        Instant dueBefore = _dueBefore.toInstant();
        LOGGER.info("Filtering Goals by timeframe, also removing any Goal with a null Due Date");
        Predicate<Goal> condition = goal -> goal.getDueDate() == null || !goal.getDueDate().isBefore(dueBefore);
        _goals.removeIf(condition);
        return _goals;
    }
}
