package com.lh.virtualDisk.services;

import com.lh.virtualDisk.controllers.DiskController;
import com.lh.virtualDisk.data.dto.CreateFolderDto;
import com.lh.virtualDisk.data.dto.FileDto;
import com.lh.virtualDisk.data.dto.FolderDto;
import com.lh.virtualDisk.exceptions.FileStorageException;
import com.lh.virtualDisk.exceptions.FolderNotFoundException;
import com.lh.virtualDisk.exceptions.InvalidCreateFolderException;
import com.lh.virtualDisk.exceptions.MyFileNotFoundException;
import com.lh.virtualDisk.mappers.ModelMapper;
import com.lh.virtualDisk.model.File;
import com.lh.virtualDisk.model.Folder;
import com.lh.virtualDisk.model.User;
import com.lh.virtualDisk.repository.FileRepository;
import com.lh.virtualDisk.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DiskService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public FolderDto createFolder(String parentFolder, CreateFolderDto folderDto) {
        if (folderDto == null || folderDto.getFolderName().trim().length() == 0)
            throw new InvalidCreateFolderException("No folderName provided");
        if (parentFolder == null || parentFolder.trim().isBlank())
            throw new InvalidCreateFolderException("No parent folder provided");
        if (folderDto.getFolderName().contains("."))
            throw new InvalidCreateFolderException("Folder name cannot contain '.'");

        User user = getCurrentUser();
        checkFolderNameExistsInUser(folderDto.getFolderName(), user);
        Folder parent = getFolderByNameAndUser(parentFolder, user);

        Folder folder = new Folder(folderDto.getFolderName(), user, parent);
        FolderDto dto = ModelMapper.parseObject(folderRepository.save(folder), FolderDto.class);
        dto.setParentFolder(parent.getName());

        dto.add(linkTo(methodOn(DiskController.class).getFolderOrFileInRoot(dto.getName())).withSelfRel());
        dto.add(linkTo(methodOn(DiskController.class).deleteFolder(dto.getName())).withSelfRel());

        return dto;
    }

    public FolderDto getFolder(String folderName) {
        try {
            Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());

            FolderDto dto = ModelMapper.parseObject(folder, FolderDto.class);
            dto.setParentFolder(folder.getParentFolder() != null ? folder.getParentFolder().getName() : null);

            dto.setSubFolders(folder.getSubFoldersNames());
            dto.setFiles(folder.getFilesNames());

            folder.getSubFoldersNames().forEach(subFolder -> {
                dto.add(linkTo(methodOn(DiskController.class).getFolderOrFileInRoot(subFolder)).withRel(subFolder));
            });
            folder.getFilesNames().forEach(file -> {
                dto.add(linkTo(methodOn(DiskController.class).getFile(dto.getName(), file)).withRel(file));
            });
            dto.add(linkTo(methodOn(DiskController.class).deleteFolder(dto.getName())).withSelfRel());

            return dto;
        } catch (Exception e) {
            throw new FolderNotFoundException(e.getMessage());
        }
    }

    public void deleteFolder(String folderName) {
        if (folderName == null || folderName.trim().length() == 0)
            throw new FolderNotFoundException("No folderName provided");

        Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());
        if (folder.getParentFolder() == null || folder.getName().equals("myDisk"))
            throw new UnsupportedOperationException("Cannot delete root folder");

        deleteFilesInMemoryInFolders(folder);

        folderRepository.delete(folder);
    }

    public long getFolderSize(String folderName) {
        Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());
        return calculateFolderSize(folder);
    }

    public FileDto uploadFile(String foldername, MultipartFile file) {
        if (foldername == null || foldername.trim().length() == 0)
            throw new FolderNotFoundException("No folderName provided");
        if (file == null)
            throw new IllegalArgumentException("No file provided");
        if (fileRepository.findByfile(file.getContentType(), file.getSize()) != null)
            throw new FileStorageException("File with name " + file.getOriginalFilename() + " already exists");

        Folder folder = getFolderByNameAndUser(foldername, getCurrentUser());
        String fileName = fileStorageService.SavedStoreFile(file);
        File fileEntity = new File(fileName, file.getContentType(), file.getSize(), folder);
        FileDto dto = ModelMapper.parseObject(fileRepository.save(fileEntity), FileDto.class);
        dto.setFolder(folder.getName());

        dto.add(linkTo(methodOn(DiskController.class).downloadFile(dto.getFolder(), dto.getName(), null)).withRel("download"));
        dto.add(linkTo(methodOn(DiskController.class).deleteFile(dto.getFolder(), dto.getName())).withRel("delete"));
        dto.add(linkTo(methodOn(DiskController.class).getFile(dto.getFolder(), dto.getName())).withSelfRel());

        return dto;
    }

    public List<FileDto> getFile(String folderName, String fileName) {
        validateFolderNameAndFileName(folderName, fileName);

        Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());
        List<File> files = fileRepository.findByFullNameAndFolder(fileName, folder);
        if (files == null || files.isEmpty())
            throw new FileStorageException("File with name " + fileName + " not found");
        List<FileDto> dtos = new ArrayList<>();
        files.forEach(file -> {
            FileDto dto = ModelMapper.parseObject(file, FileDto.class);
            dto.setFolder(folder.getName());

            dto.add(linkTo(methodOn(DiskController.class).downloadFile(dto.getFolder(), dto.getName(), null)).withRel("download"));
            dto.add(linkTo(methodOn(DiskController.class).deleteFile(dto.getFolder(), dto.getName())).withRel("delete"));

            dtos.add(dto);
        });

        return dtos;
    }

    public Resource downloadFile(String folderName, String fileName) {
        validateFolderNameAndFileName(folderName, fileName);

        Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());
        if (!folder.getFilesNames().contains(fileName))
            throw new MyFileNotFoundException("File with name " + fileName + " not found in " + folderName);

        Resource resource = fileStorageService.loadFileAsResource(fileName);
        if (resource != null)
            return resource;
        else {
            throw new MyFileNotFoundException("File with name " + fileName + " not found");
        }
    }

    public void deleteFile(String folderName, String fileName) {
        validateFolderNameAndFileName(folderName, fileName);

        Folder folder = getFolderByNameAndUser(folderName, getCurrentUser());
        if (!folder.getFilesNames().contains(fileName))
            throw new MyFileNotFoundException("File with name " + fileName + " not found in " + folderName);

        List<File> files = fileRepository.findByFullNameAndFolder(fileName, folder);
        files.forEach(fl -> {
            if (fl.getFolder().getName().equals(folder.getName()))
                fileRepository.delete(fl);
            fileStorageService.deleteFilesInMemory(fl.getName());
        });
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkFolderNameExistsInUser(String folderName, User user) {
        if (folderRepository.findByNameAndUser(folderName, user) != null)
            throw new IllegalArgumentException("Folder with name " + folderName + " already exists");
    }

    private Folder getFolderByNameAndUser(String parentFolder, User user) {
        if (parentFolder == null || user == null || parentFolder.isEmpty())
            throw new FolderNotFoundException("No folder name or user provided");
        Folder folder = folderRepository.findByNameAndUser(parentFolder, user);
        if (folder == null)
            throw new FolderNotFoundException("No folder found with name " + parentFolder + " for user " + user.getUserName());

        return folder;
    }

    private long calculateFolderSize(Folder folder) {
        if (folder == null)
            throw new FolderNotFoundException("No folder provided");
        long totalSize = 0;
        for (File file : folder.getFiles()) {
            totalSize += file.getSize();
        }
        for (Folder subFolder : folder.getSubFolders()) {
            totalSize += calculateFolderSize(subFolder);
        }
        return totalSize;
    }

    private void deleteFilesInMemoryInFolders(Folder folder) {
        if (folder == null)
            throw new FolderNotFoundException("No folder provided");

        folder.getFilesNames().forEach(file -> {
            fileStorageService.deleteFilesInMemory(file);
        });

        folder.getSubFolders().forEach(this::deleteFilesInMemoryInFolders);
    }

    private void validateFolderNameAndFileName(String folderName, String fileName) {
        if (folderName == null || folderName.isEmpty()) {
            throw new FolderNotFoundException("No folderName provided");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("No fileName provided");
        }
    }

}
