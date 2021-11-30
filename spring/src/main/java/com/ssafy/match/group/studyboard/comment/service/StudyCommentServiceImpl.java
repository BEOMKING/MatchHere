package com.ssafy.match.group.studyboard.comment.service;

import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.studyboard.article.entity.StudyArticle;
import com.ssafy.match.group.studyboard.article.repository.StudyArticleRepository;
import com.ssafy.match.group.studyboard.comment.dto.StudyArticleCommentRequestDto;
import com.ssafy.match.group.studyboard.comment.dto.StudyArticleCommentResponseDto;
import com.ssafy.match.group.studyboard.comment.entity.StudyArticleComment;
import com.ssafy.match.group.studyboard.comment.repository.StudyArticleCommentRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StudyCommentServiceImpl implements StudyCommentService{

    private final MemberRepository memberRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;

    @Transactional
    public StudyArticleCommentResponseDto create(Long articleId, Long parentId, StudyArticleCommentRequestDto dto) {
        if (parentId > 0) {
            StudyArticleComment parent = findStudyArticleComment(parentId);
            parent.addReplyCount();
        }

        StudyArticleComment studyArticleComment = StudyArticleComment.of(dto,
            findMember(SecurityUtil.getCurrentMemberId()), findArticle(articleId));
        studyArticleComment.setDepth(parentId);
        studyArticleCommentRepository.save(studyArticleComment);

        if (parentId == 0) {
            studyArticleComment.setParentId(studyArticleComment.getId());
        }

        return StudyArticleCommentResponseDto.from(studyArticleComment);
    }

    public List<StudyArticleCommentResponseDto> allComment(Long articleId) {
        return studyArticleCommentRepository.allComment(findArticle(articleId))
            .stream()
            .map(StudyArticleCommentResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public StudyArticleCommentResponseDto update(Long commentId, StudyArticleCommentRequestDto dto) {
        StudyArticleComment comment = findStudyArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return StudyArticleCommentResponseDto.from(comment);
    }

    @Transactional
    public HttpStatus delete(Long commentId) {
        StudyArticleComment comment = findStudyArticleComment(commentId);

        if (!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // 현재 댓글이 부모댓글이 아니라면 부모 댓글의 대댓글 수 감소
        if(comment.getParentId() != comment.getId()) {
            findStudyArticleComment(comment.getParentId()).removeReplyCount();
        }
        comment.setIsDeleted(true);
        return HttpStatus.OK;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public StudyArticle findArticle(Long articleId) {
        return studyArticleRepository.findById(articleId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyArticleComment findStudyArticleComment(Long commentId) {
        return studyArticleCommentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

}
