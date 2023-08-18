package com.codeday.productivity.service;

import com.codeday.productivity.entity.Reminder;
import com.codeday.productivity.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {

    private final ReminderRepository repository;

    @Autowired
    public ReminderService(ReminderRepository repository) {
        this.repository = repository;
    }

    public Reminder saveReminder(Reminder reminder) {
        return repository.save(reminder);
    }



}
