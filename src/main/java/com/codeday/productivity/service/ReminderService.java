package com.codeday.productivity.service;

import com.codeday.productivity.entity.Reminder;
import com.codeday.productivity.entity.User;
import com.codeday.productivity.repository.ReminderRepository;
import com.codeday.productivity.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReminderService {

    private static final Logger LOGGER = LogManager.getLogger(ReminderService.class);

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, UserRepository userRepository) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    public Iterable<Reminder> getUserReminders(Integer id) {
        LOGGER.info("Finding all Reminders for User ID: {}", id);
        User _user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return _user.getReminders();
    }

    public Reminder createUserReminder(Integer id, Reminder reminder) {
        LOGGER.info("Saving Reminder: {} to the Repo", reminder);
        User _user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        reminder.setUser(_user);
        return  reminderRepository.save(reminder);
    }
}
