package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateRequest;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateResult;

import java.util.List;

public interface AssetSnapshotService {
    AssetSnapshotCreateResult createSnapshot(AssetSnapshotCreateRequest request);
    
    List<AssetSnapshot> getSnapshotsByAccountId(Long accountId);
}
