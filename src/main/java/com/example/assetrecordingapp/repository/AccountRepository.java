package com.example.assetrecordingapp.repository;

import com.example.assetrecordingapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // Spring Data JPA will automatically implement basic CRUD operations
    
    // Example of custom query with sorting (uncomment if needed):
    // List<Account> findByTypeOrderByNameAsc(String type);
    
    List<Account> findByUserId(Long userId);
}
