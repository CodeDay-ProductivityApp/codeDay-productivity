package com.codeday.productivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REMINDER_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue
    private Long id;

    // relationship to User

    @Column(nullable = false)
    private String message;

    // Scheduled Time (initial time for reminder, repeats ? until marked done)
}
