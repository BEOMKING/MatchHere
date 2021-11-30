package com.ssafy.match.group.projectboard.comment.repository;

import com.ssafy.match.group.projectboard.article.entity.ProjectArticle;
import com.ssafy.match.group.projectboard.comment.entity.ProjectArticleComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectArticleCommentRepository extends JpaRepository<ProjectArticleComment, Long> {

    @Query("select sac from ProjectArticleComment sac where sac.projectArticle = :projectArticle order by sac.parentId, sac.createDate")
    List<ProjectArticleComment> allComment(@Param("projectArticle") ProjectArticle projectArticle);

    @Transactional
    @Modifying
    void deleteAllByProjectArticle(@Param("projectArticle") ProjectArticle projectArticle);
}