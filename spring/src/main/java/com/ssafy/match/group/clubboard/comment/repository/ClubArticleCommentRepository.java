package com.ssafy.match.group.clubboard.comment.repository;

import com.ssafy.match.group.clubboard.article.entity.ClubArticle;
import com.ssafy.match.group.clubboard.comment.entity.ClubArticleComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubArticleCommentRepository extends JpaRepository<ClubArticleComment, Long> {

    @Query("select sac from ClubArticleComment sac order by sac.parentId, sac.createDate")
    List<ClubArticleComment> allComment(@Param("clubArticle") ClubArticle clubArticle);
}