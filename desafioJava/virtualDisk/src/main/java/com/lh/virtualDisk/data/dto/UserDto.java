package com.lh.virtualDisk.data.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class UserDto extends RepresentationModel<UserDto> {
    private Long id;

    private String username;

    private String fullname;

    private String password;

    private List<String> permissions;

    public UserDto() {
    }

    public void addPermission(String permission) {
        if (this.permissions != null) this.permissions.add(permission);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
