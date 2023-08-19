package com.codeday.productivity.controller;

import com.codeday.productivity.entity.Reminder;
import com.codeday.productivity.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/{userId}/reminder")
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public Reminder createUserReminder(@PathVariable Integer userId, @RequestBody Reminder reminder) {
        return reminderService.createUserReminder(userId, reminder);
    }

    @GetMapping
    public Iterable<Reminder> getUserReminders(@PathVariable Integer userId) {
        return reminderService.getUserReminders(userId);
    }
}
