package com.ssafy.match.file.service;

import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.file.dto.DeleteFileRequestDto;
import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.file.exception.FileStorageException;
import com.ssafy.match.file.exception.MyFileNotFoundException;
import com.ssafy.match.file.repository.DBFileRepository;
import com.ssafy.match.group.project.entity.Project;
import com.ssafy.match.group.project.repository.ProjectRepository;
import com.ssafy.match.group.study.entity.Study;
import com.ssafy.match.group.study.repository.StudyRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DBFileStorageService {

    private final DBFileRepository dbFileRepository;
    private final ProjectRepository projectRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public DBFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());

            return makeDownloadUri(dbFileRepository.save(dbFile));
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }

    @Transactional
    public DBFile makeDownloadUri(DBFile dbFile){
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/file/downloadFile/")
            .path(dbFile.getId())
            .toUriString();

        dbFile.setDownload_uri(fileDownloadUri);
        return dbFile;
    }

    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
            .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    @Transactional
    public HttpStatus projectDeleteFile(DeleteFileRequestDto dto){
        Project project = projectRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
        String uuid = project.getCoverPic().getId();
        project.initialCoverPic();
        dbFileRepository.deleteById(uuid);
        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus studyDeleteFile(DeleteFileRequestDto dto){
        Study study = studyRepository.findById(dto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        String uuid = study.getCoverPic().getId();
        study.initialCoverPic();
        dbFileRepository.deleteById(uuid);
        return HttpStatus.OK;
    }
}
