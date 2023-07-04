package com.lh.virtualDisk.data.dto.security;

public class AccountCredentialsDto {
    private String username;
    private String password;

    public AccountCredentialsDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
