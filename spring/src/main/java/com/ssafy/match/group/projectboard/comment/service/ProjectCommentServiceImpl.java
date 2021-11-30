package com.ssafy.match.group.projectboard.comment.service;

import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.projectboard.article.entity.ProjectArticle;
import com.ssafy.match.group.projectboard.article.repository.ProjectArticleRepository;
import com.ssafy.match.group.projectboard.comment.dto.ProjectArticleCommentRequestDto;
import com.ssafy.match.group.projectboard.comment.dto.ProjectArticleCommentResponseDto;
import com.ssafy.match.group.projectboard.comment.entity.ProjectArticleComment;
import com.ssafy.match.group.projectboard.comment.repository.ProjectArticleCommentRepository;
import com.ssafy.match.group.projectboard.comment.service.ProjectCommentService;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ProjectCommentServiceImpl implements ProjectCommentService {

    private final MemberRepository memberRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;

    @Transactional
    public ProjectArticleCommentResponseDto create(Long articleId, Long parentId, ProjectArticleCommentRequestDto dto) {
        if (parentId > 0) {
            ProjectArticleComment parent = findProjectArticleComment(parentId);
            parent.addReplyCount();
        }

        ProjectArticleComment projectArticleComment = ProjectArticleComment.of(dto,
            findMember(SecurityUtil.getCurrentMemberId()), findArticle(articleId));
        projectArticleComment.setDepth(parentId);
        projectArticleCommentRepository.save(projectArticleComment);

        if (parentId == 0) {
            projectArticleComment.setParentId(projectArticleComment.getId());
        }

        return ProjectArticleCommentResponseDto.from(projectArticleComment);
    }

    public List<ProjectArticleCommentResponseDto> allComment(Long articleId) {
        return projectArticleCommentRepository.allComment(findArticle(articleId))
            .stream()
            .map(ProjectArticleCommentResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public ProjectArticleCommentResponseDto update(Long commentId, ProjectArticleCommentRequestDto dto) {
        ProjectArticleComment comment = findProjectArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return ProjectArticleCommentResponseDto.from(comment);
    }

    @Transactional
    public HttpStatus delete(Long commentId) {
        ProjectArticleComment comment = findProjectArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 현재 댓글이 부모댓글이 아니라면 부모 댓글의 대댓글 수 감소
        if(comment.getParentId() != comment.getId()) {
            findProjectArticleComment(comment.getParentId()).removeReplyCount();
        }
        comment.setIsDeleted(true);
        return HttpStatus.OK;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public ProjectArticle findArticle(Long articleId) {
        return projectArticleRepository.findById(articleId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public ProjectArticleComment findProjectArticleComment(Long commentId) {
        return projectArticleCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

}
