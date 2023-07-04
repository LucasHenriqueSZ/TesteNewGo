package com.lh.virtualDisk.repository;

import com.lh.virtualDisk.model.File;
import com.lh.virtualDisk.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.type = :type AND f.size = :size")
    File findByfile(@Param("type") String type, @Param("size") long size);

    @Query("SELECT f FROM File f WHERE f.name = :name AND f.folder = :folder")
    List<File> findByFullNameAndFolder(@Param("name") String fullName, @Param("folder") Folder folder);
}