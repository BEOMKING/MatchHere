package com.ssafy.match.group.studyboard.article.repository;


import com.ssafy.match.group.studyboard.article.entity.StudyArticle;
import com.ssafy.match.group.studyboard.article.entity.StudyContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyContentRepository extends JpaRepository<StudyContent, Long> {
    Optional<StudyContent> getByStudyArticle(StudyArticle studyArticle);
}
