package com.lh.virtualDisk.controllers;

import com.lh.virtualDisk.data.dto.CreateFolderDto;
import com.lh.virtualDisk.data.dto.FileDto;
import com.lh.virtualDisk.data.dto.FolderDto;
import com.lh.virtualDisk.exceptions.MyFileNotFoundException;
import com.lh.virtualDisk.services.DiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/myDisk")
@Tag(name = "Disk", description = "endpoints for managing disk")
public class DiskController {

    @Autowired
    private DiskService diskService;

    @PostMapping("/createFolder")
    @Operation(summary = "Create a folder in the root of the disk", description = "Create a folder in the root of the disk", tags = {"Disk"})
    public ResponseEntity<FolderDto> createFolderInRoot(@RequestBody CreateFolderDto folderDto) {
        return createFolder("myDisk", folderDto);
    }

    @PostMapping("/{Folder}/createFolder")
    @Operation(summary = "Create a folder in the specified folder", description = "Create a folder in the specified folder", tags = {"Disk"})
    public ResponseEntity<FolderDto> createFolder(@PathVariable(value = "Folder") String parentFolder, @RequestBody CreateFolderDto folderDto) {
        FolderDto dto = diskService.createFolder(parentFolder, folderDto);
        URI location = URI.create(String.format("/myDisk/%s", dto.getName()));
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping
    @Operation(summary = "Get the root folder", description = "Get the root folder", tags = {"Disk"})
    public ResponseEntity<FolderDto> getFolderRoot() {
        return ResponseEntity.ok(diskService.getFolder("myDisk"));
    }

    @DeleteMapping("/{Folder}/deleteFolder")
    @Operation(summary = "Delete a folder in the specified folder", description = "Delete a folder in the specified folder", tags = {"Disk"})
    public ResponseEntity<?> deleteFolder(@PathVariable(value = "Folder") String folderName) {
        diskService.deleteFolder(folderName);
        return ResponseEntity.ok("Folder deleted successfully");
    }

    @GetMapping("/{Folder}/folderSize")
    @Operation(summary = "Get the size of the specified folder", description = "Get the size of the specified folder", tags = {"Disk"})
    public ResponseEntity<Long> getFolderSize(@PathVariable(value = "Folder") String folderName) {
        return ResponseEntity.ok(diskService.getFolderSize(folderName));
    }

    @GetMapping("/folderSize")
    @Operation(summary = "Get the size of the root folder", description = "Get the size of the root folder", tags = {"Disk"})
    public ResponseEntity<Long> getFolderSizeRoot() {
        return ResponseEntity.ok(diskService.getFolderSize("myDisk"));
    }

    @GetMapping("/{component}")
    @Operation(summary = "Get a folder or file in the root of the disk", description = "Get a folder or file in the root of the disk", tags = {"Disk"})
    public ResponseEntity<?> getFolderOrFileInRoot(@PathVariable(value = "component") String component) {
        if (component.contains(".")) {
            return getFile("myDisk", component);
        } else {
            return ResponseEntity.ok(diskService.getFolder(component));
        }
    }

    @GetMapping("/{File:.+}/downloadFile")
    @Operation(summary = "Download a file in the root of the disk", description = "Download a file in the root of the disk", tags = {"Disk"})
    public ResponseEntity<Resource> downloadFileInRoot(@PathVariable(value = "File") String fileName, HttpServletRequest request) {
        return downloadFile("myDisk", fileName, request);
    }

    @GetMapping("/{Folder}/{File:.+}/downloadFile")
    @Operation(summary = "Download a file in the specified folder", description = "Download a file in the specified folder", tags = {"Disk"})
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "Folder") String folderName, @PathVariable(value = "File") String fileName, HttpServletRequest request) {
        Resource resource = diskService.downloadFile(folderName, fileName);
        String contentType = "";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            throw new MyFileNotFoundException("Could not determine file type.");
        }

        if (contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/{Folder}/{File}")
    @Operation(summary = "Get a file in the specified folder", description = "Get a file in the specified folder", tags = {"Disk"})
    public ResponseEntity<List<FileDto>> getFile(@PathVariable(value = "Folder") String folderName, @PathVariable(value = "File") String fileName) {
        return ResponseEntity.ok(diskService.getFile(folderName, fileName));
    }

    @PostMapping("/uploadFile")
    @Operation(summary = "Upload a file in the root of the disk", description = "Upload a file in the root of the disk", tags = {"Disk"})
    public ResponseEntity<FileDto> uploadFileInRoot(@RequestParam("file") MultipartFile file) {
        return uploadFile("myDisk", file);
    }

    @PostMapping("/{Folder}/uploadFile")
    @Operation(summary = "Upload a file in the specified folder", description = "Upload a file in the specified folder", tags = {"Disk"})
    public ResponseEntity<FileDto> uploadFile(@PathVariable(value = "Folder") String folderName, @RequestParam("file") MultipartFile file) {
        URI location = URI.create(String.format("/myDisk/%s/%s", folderName, file.getOriginalFilename()));
        return ResponseEntity.created(location).body(diskService.uploadFile(folderName, file));
    }

    @DeleteMapping("/{File}/deleteFile")
    @Operation(summary = "Delete a file in the root of the disk", description = "Delete a file in the root of the disk", tags = {"Disk"})
    public ResponseEntity<?> deleteFileInRoot(@PathVariable(value = "File") String fileName) {
        return deleteFile("myDisk", fileName);
    }

    @DeleteMapping("/{Folder}/{File}/deleteFile")
    @Operation(summary = "Delete a file in the specified folder", description = "Delete a file in the specified folder", tags = {"Disk"})
    public ResponseEntity<?> deleteFile(@PathVariable(value = "Folder") String folderName, @PathVariable(value = "File") String fileName) {
        diskService.deleteFile(folderName, fileName);
        return ResponseEntity.ok("File deleted successfully");
    }
}
