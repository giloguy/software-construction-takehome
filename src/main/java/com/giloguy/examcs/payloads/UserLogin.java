package com.giloguy.examcs.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class UserLogin {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public UserLogin() {
    }

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
