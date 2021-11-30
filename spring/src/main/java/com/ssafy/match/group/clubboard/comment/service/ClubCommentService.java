package com.ssafy.match.group.clubboard.comment.service;

import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentRequestDto;
import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface ClubCommentService {

    Long create(Long articleId, Long parentId, ClubArticleCommentRequestDto dto);

    List<ClubArticleCommentResponseDto> allComment(Long articleId);

    HttpStatus update(Long commentId, ClubArticleCommentRequestDto dto) throws Exception;

    HttpStatus delete(Long commentId) throws Exception;
}
