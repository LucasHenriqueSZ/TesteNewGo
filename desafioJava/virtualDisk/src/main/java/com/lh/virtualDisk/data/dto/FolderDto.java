package com.lh.virtualDisk.data.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class FolderDto extends RepresentationModel<FolderDto> {

    private String name;

    private String user;

    private String parentFolder;

    private List<String> subFolders;

    private List<String> files;

    public FolderDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<String> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<String> subFolders) {
        this.subFolders = subFolders;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
