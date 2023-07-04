package com.lh.virtualDisk.data.dto;

import org.springframework.hateoas.RepresentationModel;

public class FileDto extends RepresentationModel<FileDto> {

    private String name;
    private String type;
    private long size;
    private String folder;

    public FileDto() {
    }

    public FileDto(String name, String type, long size, String folder) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
