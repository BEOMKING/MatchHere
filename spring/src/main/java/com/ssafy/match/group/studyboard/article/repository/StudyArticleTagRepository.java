package com.ssafy.match.group.studyboard.article.repository;

import com.ssafy.match.group.studyboard.article.entity.StudyArticle;
import com.ssafy.match.group.studyboard.article.entity.StudyArticleTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StudyArticleTagRepository extends JpaRepository<StudyArticleTag, Long> {

    // 현재 스터디의 태그 조회
    List<StudyArticleTag> findAllByStudyArticle(StudyArticle studyArticle);

    // 스터디 게시글의 기존 태그 전체 제거
    @Transactional
    @Modifying
    @Query("delete from StudyArticleTag pat where pat.studyArticle = :studyArticle")
    void deleteAllTagsByStudyArticle(@Param("studyArticle") StudyArticle studyArticle);
}