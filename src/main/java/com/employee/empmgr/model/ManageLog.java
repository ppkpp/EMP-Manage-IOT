package com.employee.empmgr.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "manage_log")
public class ManageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actions")
    private String actions;

    @Column(name = "username")
    private String username;

    @Column(name = "position")
    private String position;

    private LocalDate createDate;
    private LocalTime createTime;

    // Default constructor
    public ManageLog() {
    }

    // Parameterized constructor
    public ManageLog(String actions, String username, String position) {
        this.actions = actions;
        this.username = username;
        this.position = position;
        this.createDate = LocalDate.now();
        this.createTime = LocalTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalTime createTime) {
        this.createTime = createTime;
    }

    // Lifecycle callback
    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDate.now();
        }
        if (this.createTime == null) {
            this.createTime = LocalTime.now();
        }
    }
}
