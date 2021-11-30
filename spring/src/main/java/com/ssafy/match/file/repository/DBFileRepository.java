package com.ssafy.match.file.repository;

import com.ssafy.match.file.entity.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DBFileRepository extends JpaRepository<DBFile, String> {

    @Transactional
    @Modifying
    @Query("delete from matching.files f where f.download_uri = :fileDownloadUri")
    void deleteByFileDownloadUri(@Param("fileDownloadUri") String fileDownloadUri);
}