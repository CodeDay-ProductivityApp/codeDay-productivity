package com.codeday.productivity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "GOAL_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue
    private int id;

    private String title;

    private String description;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant startDate;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant endDate;

    @Column(name = "due_date", columnDefinition = "TIMESTAMP")
    private Instant dueDate;

    @Column(name = "is_complete", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String isComplete;

    @Column(name = "progress")
    private Double progress;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP")
    private Instant lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value="user-goal")
    private User user;
}
