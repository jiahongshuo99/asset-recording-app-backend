package com.example.assetrecordingapp.payload;

import lombok.Data;

@Data
public class AccountAmountUpdateRequest {
    private Long accountId;
    private String amount;
}
