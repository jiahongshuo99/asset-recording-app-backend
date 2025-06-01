package com.example.assetrecordingapp.payload;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    private String name;
    private String type;
    private String remark;
}
