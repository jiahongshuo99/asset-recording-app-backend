package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/account/snapshots")
public class AssetSnapshotController {

    @Resource
    private AssetSnapshotService assetSnapshotService;

    @GetMapping("/{accountId}")
    @RequireLogin
    public HttpResponse<List<AssetSnapshot>> getSnapshotsByAccountId(@PathVariable Long accountId) {
        List<AssetSnapshot> snapshots = assetSnapshotService.getSnapshotsByAccountId(accountId);
        return HttpResponse.success(snapshots);
    }
}
