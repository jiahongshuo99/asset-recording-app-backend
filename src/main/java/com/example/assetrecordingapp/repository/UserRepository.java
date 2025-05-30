package com.example.assetrecordingapp.repository;

import com.example.assetrecordingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
    Optional<User> findByPhone(String phone);
    
    @Query("SELECT MAX(u.userId) FROM User u")
    Long findMaxUserId();
}
