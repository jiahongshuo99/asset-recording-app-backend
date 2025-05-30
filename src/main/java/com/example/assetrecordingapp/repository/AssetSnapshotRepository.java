package com.example.assetrecordingapp.repository;

import com.example.assetrecordingapp.model.AssetSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetSnapshotRepository extends JpaRepository<AssetSnapshot, Long> {
    List<AssetSnapshot> findByAccountIdOrderBySnapshotTimeDesc(Long accountId);


}
