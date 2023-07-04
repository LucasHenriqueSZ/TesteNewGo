package com.lh.virtualDisk.repository;

import com.lh.virtualDisk.model.Folder;
import com.lh.virtualDisk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("SELECT f FROM Folder f WHERE f.name = :parentFolder AND f.user = :user")
    Folder findByNameAndUser(@Param("parentFolder") String parentFolder, @Param("user") User user);
}
