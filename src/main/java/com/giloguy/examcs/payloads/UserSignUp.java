package com.giloguy.examcs.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class UserSignUp {

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    
    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String password;

    public UserSignUp() {
    }

    public UserSignUp(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

}
