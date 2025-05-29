package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/snapshots")
public class AssetSnapshotController {

    @Resource
    private AssetSnapshotService assetSnapshotService;

    @PostMapping
    @RequireLogin
    public HttpResponse<AssetSnapshot> addSnapshot(
            @PathVariable Long accountId,
            @RequestBody AssetSnapshot snapshot) {
        AssetSnapshot createdSnapshot = assetSnapshotService.addSnapshot(snapshot);
        return HttpResponse.success(createdSnapshot);
    }

    @GetMapping
    @RequireLogin
    public HttpResponse<List<AssetSnapshot>> getSnapshots(
            @PathVariable Long accountId) {
        List<AssetSnapshot> snapshots = assetSnapshotService.getSnapshotsByAccount(accountId);
        return HttpResponse.success(snapshots);
    }
}
