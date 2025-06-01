package com.example.assetrecordingapp.payload;

import lombok.Data;

@Data
public class AccountCreateRequest {
    private String name;

    private String amount;
    
    // Keep currentAmount as optional cache field if needed
    private String currentAmount;

    private String type;

    private String remark;

    private Long createdTime;

    private Long updatedTime;
}
