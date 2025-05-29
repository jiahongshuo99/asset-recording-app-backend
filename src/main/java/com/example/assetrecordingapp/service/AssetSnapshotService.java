package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.model.AssetSnapshot;
import java.util.List;

public interface AssetSnapshotService {
    AssetSnapshot addSnapshot(AssetSnapshot snapshot);
    List<AssetSnapshot> getSnapshotsByAccount(Long accountId);
}
