package com.example.assetrecordingapp.repository;

import com.example.assetrecordingapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Alternative explicit version
    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.isDeleted = 0")
    List<Account> findActiveAccountsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a FROM Account a WHERE a.id = :id AND a.isDeleted = 0")
    Optional<Account> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.userId = :userId AND a.isDeleted = 0")
    long countActiveByUserId(@Param("userId") Long userId);
}
