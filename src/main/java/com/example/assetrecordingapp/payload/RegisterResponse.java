package com.example.assetrecordingapp.payload;

public class RegisterResponse {
    private Long accountId;
    private String username;
    private String phone;
    private String token;

    public RegisterResponse(Long accountId, String username, String phone, String token) {
        this.accountId = accountId;
        this.username = username;
        this.phone = phone;
        this.token = token;
    }


    public Long getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }
}
