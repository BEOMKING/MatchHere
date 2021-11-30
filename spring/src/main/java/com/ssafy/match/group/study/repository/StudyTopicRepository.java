package com.ssafy.match.group.study.repository;

import com.ssafy.match.group.study.entity.Study;
import com.ssafy.match.group.study.entity.StudyTopic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StudyTopicRepository extends JpaRepository<StudyTopic, Long> {
    // 스터디의 기존 주제 전체 제거
    @Transactional
    @Modifying
    @Query("delete from StudyTopic st where st.study = :study")
    void deleteAllByStudy(@Param("study") Study study);

    List<StudyTopic> findAllByStudy(@Param("study") Study study);
}