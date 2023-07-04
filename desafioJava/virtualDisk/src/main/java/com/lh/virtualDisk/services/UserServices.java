package com.lh.virtualDisk.services;

import com.lh.virtualDisk.controllers.DiskController;
import com.lh.virtualDisk.controllers.UserController;
import com.lh.virtualDisk.data.dto.UserDto;
import com.lh.virtualDisk.exceptions.InavlidUserCreationException;
import com.lh.virtualDisk.mappers.ModelMapper;
import com.lh.virtualDisk.model.Folder;
import com.lh.virtualDisk.model.Permission;
import com.lh.virtualDisk.model.User;
import com.lh.virtualDisk.repository.FolderRepository;
import com.lh.virtualDisk.repository.PermissionRepository;
import com.lh.virtualDisk.repository.UserRepository;
import com.lh.virtualDisk.util.EncryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUserName(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User: " + username + " not found");
        }
    }

    public UserDto createUser(UserDto userDto) {
        checkUser(userDto);
        User newUser = ModelMapper.parseObject(userDto, User.class);
        newUser.setPermissions(listRolesToPermissions(userDto.getPermissions()));
        newUser.setPassword(EncryptPassword.encrypt(userDto.getPassword()).substring("{pbkdf2}".length()));
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);

        User savedUser = userRepository.save(newUser);

        Folder rootDirectory = new Folder("myDisk", savedUser, null);
        folderRepository.save(rootDirectory);

        UserDto dto = ModelMapper.parseObject(savedUser, UserDto.class);
        dto.setPassword(null);
        dto.setPermissions(userDto.getPermissions());
        dto.add(linkTo(methodOn(UserController.class).findById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(DiskController.class).getFolderRoot()).withRel("rootDirectory"));

        return dto;
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDto dto = ModelMapper.parseObject(user, UserDto.class);
        dto.setPassword(null);
        dto.setPermissions(user.getPermissions().stream().map(Permission::getDescription).toList());
        dto.add(linkTo(methodOn(UserController.class).findById(dto.getId())).withSelfRel());

        return dto;
    }

    private void checkUser(UserDto user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InavlidUserCreationException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InavlidUserCreationException("Password is required");
        }
        if (user.getPassword().length() < 5) {
            throw new InavlidUserCreationException("Password must be at least 5 characters");
        }
        if (user.getFullname() == null || user.getFullname().isEmpty()) {
            throw new InavlidUserCreationException("Full name is required");
        }
        if (user.getPermissions() == null || user.getPermissions().get(0).isBlank()) {
            throw new InavlidUserCreationException("Roles is required");
        }
        if (userRepository.findByUserName(user.getUsername()) != null) {
            throw new InavlidUserCreationException("User already exists");
        }
    }

    private List<Permission> listRolesToPermissions(List<String> permissions) {
        List<Permission> permissionsList = new ArrayList<>();
        for (String permission : permissions) {
            Permission perms = permissionRepository.findByDescription(permission);
            if (perms != null) {
                permissionsList.add(perms);
            } else {
                throw new InavlidUserCreationException("invalid roles");
            }
        }

        return permissionsList;
    }
}
