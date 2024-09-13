package com.employee.empmgr.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "reporttype")
    private String reporttype;

    private LocalDate createDate;
    private LocalTime createTime;

    @Column(name = "status")
    private String status="PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Report() {
        // Default constructor
    }

    public Report(String title, String description, String reporttype, LocalDate createDate, LocalTime createTime,
            String status) {
        this.title = title;
        this.description = description;
        this.reporttype = reporttype;
        this.createDate = createDate;
        this.createTime = createTime;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDate.now();
        }
        if (this.createTime == null) {
            this.createTime = LocalTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporttype() {
        return reporttype;
    }

    public void setReporttype(String reporttype) {
        this.reporttype = reporttype;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
