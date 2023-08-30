package com.codeday.productivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

/**
 * Represents a User entity in the system. This entity is mapped to the "USER_TBL" table
 * in the database. It contains information about the user's identity, status, and
 * timestamps regarding their creation and last update.
 *
 * <p>
 * The class uses Lombok annotations for getter and setter methods,
 * and JPA annotations for defining the mapping to database table and columns.
 * </p>
 * @author Nahom Alemu
 * @version 1.0
 *
 */
@Entity
@Table(name = "USER_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * The unique identifier for a user. This field is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
    private String isActive;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdOn;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant lastUpdated;

    /**
     * This method is called before persisting an object, to ensure 'isActive' is set.
     */
    @PrePersist
    public void prePersist() {
        if (isActive == null) { // check for null
            this.isActive = "Y";
        }
    }
}

