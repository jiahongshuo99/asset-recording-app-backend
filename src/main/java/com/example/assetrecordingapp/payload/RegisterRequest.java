package com.example.assetrecordingapp.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank
    @Size(min = 11, max = 20, message = "Phone number must be between 11-20 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String phone;
    
    @NotBlank
    @Size(min = 6, max = 40, message = "Password must be between 6-40 characters")
    private String password;
    
    @NotBlank
    @Size(min = 2, max = 64, message = "Username must be between 2-64 characters")
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
