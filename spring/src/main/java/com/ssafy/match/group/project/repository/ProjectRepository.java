package com.ssafy.match.group.project.repository;

import com.ssafy.match.common.entity.ProjectProgressState;
import com.ssafy.match.common.entity.PublicScope;
import com.ssafy.match.common.entity.RecruitmentState;
import com.ssafy.match.group.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 모든 프로젝트
    @Query("select p from matching.project p "
        + "where p.projectProgressState <> :projectProgressState and p.recruitmentState = :recruitmentState and p.publicScope = :publicScope and p.isActive = true")
    Page<Project> findAllProject(
        @Param("projectProgressState") ProjectProgressState projectProgressState,
        @Param("recruitmentState") RecruitmentState recruitmentState,
        @Param("publicScope") PublicScope publicScope,
        Pageable pageable);

    // 추천 프로젝트
//    List<Project> findAll();
}