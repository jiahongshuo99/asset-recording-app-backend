package com.example.assetrecordingapp.model;

import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;
    
    @Column(name = "username", nullable = false, length = 64)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "phone", unique = true, nullable = false, length = 20)
    private String phone;
    
    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;
    
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Timestamp updatedTime;
}
