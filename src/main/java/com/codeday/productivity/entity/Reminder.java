package com.codeday.productivity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reminder_sequence")
    @SequenceGenerator(name = "reminder_sequence", sequenceName = "reminder_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value="user-reminder")
    private User user;

    @Column(nullable = false)
    private String message;

    // Scheduled Time (initial time for reminder, repeats ? until marked done)
}
