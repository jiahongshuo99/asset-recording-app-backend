package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.model.AssetSnapshot;

import java.util.List;

public interface AssetSnapshotService {
    List<AssetSnapshot> getSnapshotsByAccountId(Long accountId);
}
