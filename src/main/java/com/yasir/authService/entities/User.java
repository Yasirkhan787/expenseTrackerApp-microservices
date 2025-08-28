package com.yasir.authService.entities;

import jakarta.persistence.*;

@Entity // Marks this class as a JPA entity (table in DB)
public class User {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.AUTO) // Auto-generate ID value
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING) // Saves enum name (e.g. "ADMIN") instead of number
    @Column(nullable = false) // This field cannot be null in DB
    private Role role; // Each user has ONE role only

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
