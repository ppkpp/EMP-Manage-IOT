package com.employee.empmgr.model;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
  @Column(name = "name", unique = true) // Ensures uniqueness in database
  private String name;

  @Column(name = "password")
  private String password;
  
  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "dob")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate dob;

  @Column(name = "gender")
  private String gender;

  @Column(name = "email")
  private String email;

  @Column(name = "address")
  private String address;

  @Column(name = "position")
  private String position;

  @Column(name = "hiredate")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate hiredate;

  @Column(name = "department")
  private String department;

  @Column(name = "salary")
  private String salary;

  @Column(name = "status")
  private String status;

  @Column(name = "rfid")
  private String rfid;

  @Column(name = "phone")
  private String phone;
 
   @Column(name = "code")
  private String code;

  @Column(name = "image_url")
  private String imageUrl;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Attendent> attendents = new HashSet<>();


  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Report> reports = new HashSet<>();

  public Employee() {
    // Default constructor required by JPA
  }

  @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


  public Employee(String code ,String name, String firstname, String lastname, LocalDate dob, String gender, String email,
      String address, String position, LocalDate hiredate, String department, String salary, String status,
      String rfid, String phone, String imageUrl) {
        this.code = code ;
    this.name = name;
    this.firstname = firstname;
    this.lastname = lastname;
    this.dob = dob;
    this.gender = gender;
    this.email = email;
    this.address = address;
    this.position = position;
    this.hiredate = hiredate;
    this.department = department;
    this.salary = salary;
    this.status = status;
    this.rfid = rfid;
    this.phone = phone;
    this.imageUrl = imageUrl;
  }
  
  // Getters and Setters
  public Set<Report> getReports() {
    return reports;
  }

  public void setReports(Set<Report> reports) {
    this.reports = reports;
  }
  // Getters and Setters
  public Set<Attendent> getAttendents() {
    return attendents;
  }

  public void setAttendents(Set<Attendent> attendents) {
    this.attendents = attendents;
  }
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public LocalDate getHiredate() {
    return hiredate;
  }

  public void setHiredate(LocalDate hiredate) {
    this.hiredate = hiredate;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRfid() {
    return rfid;
  }

  public void setRfid(String rfid) {
    this.rfid = rfid;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "Employee [id=" + id + ", name=" + name + ", firstname=" + firstname + ", lastname=" + lastname + ", dob="
        + dob + ", gender=" + gender + ", email=" + email + ", address=" + address + ", position=" + position
        + ", hiredate=" + hiredate + ", department=" + department + ", salary=" + salary + ", status=" + status
        + ", rfid=" + rfid + "]";
  }

  // public Employee orElseThrow(Object object) {
  //   // TODO Auto-generated method stub
  //   throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
  // }
}
