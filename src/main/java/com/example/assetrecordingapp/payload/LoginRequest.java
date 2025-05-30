package com.example.assetrecordingapp.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    
    @NotBlank
    @Size(min = 11, max = 20, message = "Phone number must be between 11-20 characters")
    private String phone;
    
    @NotBlank
    @Size(min = 6, max = 40, message = "Password must be between 6-40 characters")
    private String password;

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
