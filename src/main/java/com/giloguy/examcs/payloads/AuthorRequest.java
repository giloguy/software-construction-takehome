package com.giloguy.examcs.payloads;

import jakarta.validation.constraints.NotBlank;
public class AuthorRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
