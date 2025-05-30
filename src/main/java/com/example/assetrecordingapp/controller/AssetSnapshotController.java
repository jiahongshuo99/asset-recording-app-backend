package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateRequest;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateResult;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/snapshots")
public class AssetSnapshotController {

    @Resource
    private AssetSnapshotService assetSnapshotService;

    @PostMapping
    @RequireLogin
    public HttpResponse<AssetSnapshotCreateResult> createSnapshot(@RequestBody AssetSnapshotCreateRequest request) {
        AssetSnapshotCreateResult result = assetSnapshotService.createSnapshot(request);
        return HttpResponse.success(result);
    }

    @GetMapping("/account/{accountId}")
    @RequireLogin
    public HttpResponse<List<AssetSnapshot>> getSnapshotsByAccountId(@PathVariable Long accountId) {
        List<AssetSnapshot> snapshots = assetSnapshotService.getSnapshotsByAccountId(accountId);
        return HttpResponse.success(snapshots);
    }
}
