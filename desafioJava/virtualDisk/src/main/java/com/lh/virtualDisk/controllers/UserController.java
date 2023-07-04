package com.lh.virtualDisk.controllers;

import com.lh.virtualDisk.data.dto.UserDto;
import com.lh.virtualDisk.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "endpoints for managing users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/create")
    @Operation(summary = "Create user", description = "Create user", tags = {"User"})
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        UserDto user = userServices.createUser(userDto);
        URI location = URI.create(String.format("/user/%s", user.getId()));
        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id, only admins can do this", description = "Get user by id, only admins can do this", tags = {"User"})
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        UserDto user = userServices.findById(id);
        return ResponseEntity.ok(user);
    }

}
