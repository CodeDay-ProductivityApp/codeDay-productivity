package com.codeday.productivity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "USER_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
    private String isActive;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdOn;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant lastUpdated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value="user-goal")
    private List<Goal> goals = new ArrayList<>();

}

