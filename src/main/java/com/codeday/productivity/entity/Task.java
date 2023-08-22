package com.codeday.productivity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TASK_TBL")
public class Task {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant startDate;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant endDate;

    @Column(name = "is_complete", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String isComplete;

    @Column(name = "Progress")
    private Double progress;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP")
    private Instant lastUpdated;

    @Column(name = "TimeSpent")
    private Long timeSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    @JsonBackReference(value="goal-task")
    private Goal goal;

}


