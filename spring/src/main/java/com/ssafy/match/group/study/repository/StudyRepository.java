package com.ssafy.match.group.study.repository;

import com.ssafy.match.common.entity.PublicScope;
import com.ssafy.match.common.entity.RecruitmentState;
import com.ssafy.match.common.entity.StudyProgressState;
import com.ssafy.match.group.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select s from matching.study s "
        + "where s.studyProgressState <> :studyProgressState and s.recruitmentState = :recruitmentState and s.publicScope = :publicScope and s.isActive = true")
    Page<Study> findAllStudy(
        @Param("studyProgressState") StudyProgressState studyProgressState,
        @Param("recruitmentState") RecruitmentState recruitmentState,
        @Param("publicScope") PublicScope publicScope,
        Pageable pageable);

}