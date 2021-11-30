package com.ssafy.match.group.clubboard.comment.service;

import com.ssafy.match.group.clubboard.article.entity.ClubArticle;
import com.ssafy.match.group.clubboard.article.repository.ClubArticleRepository;
import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentRequestDto;
import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentResponseDto;
import com.ssafy.match.group.clubboard.comment.entity.ClubArticleComment;
import com.ssafy.match.group.clubboard.comment.repository.ClubArticleCommentRepository;
import com.ssafy.match.group.clubboard.comment.service.ClubCommentService;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ClubCommentServiceImpl implements ClubCommentService {

    private final MemberRepository memberRepository;
    private final ClubArticleRepository clubArticleRepository;
    private final ClubArticleCommentRepository clubArticleCommentRepository;

    @Transactional
    public Long create(Long articleId, Long parentId, ClubArticleCommentRequestDto dto){
        if(parentId > 0) {
            ClubArticleComment parent = clubArticleCommentRepository.findById(parentId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 부모 댓글입니다."));
            parent.addReplyCount();
        }

        ClubArticleComment clubArticleComment = ClubArticleComment.builder()
            .member(findMember(SecurityUtil.getCurrentMemberId()))
            .clubArticle(findArticle(articleId))
            .parentId(parentId)
            .clubArticleCommentRequestDto(dto)
            .build();

        clubArticleCommentRepository.save(clubArticleComment);

        if(parentId == 0){
            clubArticleComment.setParentId(clubArticleComment.getId());
        }

        return clubArticleComment.getId();
    }

    public List<ClubArticleCommentResponseDto> allComment(Long articleId){
        List<ClubArticleComment> clubArticleComments = clubArticleCommentRepository.allComment(findArticle(articleId));
        List<ClubArticleCommentResponseDto> result = new ArrayList<>();

        for (ClubArticleComment sac: clubArticleComments) {
            result.add(ClubArticleCommentResponseDto.builder()
                .id(sac.getId())
                .content(sac.getContent())
                .nickname(sac.getMember().getNickname())
                .isModified(sac.getIsModified())
                .isDeleted(sac.getIsDeleted())
                .parentId(sac.getParentId())
                .depth(sac.getDepth())
                .replyCount(sac.getReplyCount())
                .build());
        }

        return result;
    }

    @Transactional
    public HttpStatus update(Long commentId, ClubArticleCommentRequestDto dto) throws Exception {
        ClubArticleComment comment = clubArticleCommentRepository.findById(commentId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));

        if(!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())){
            throw new Exception("권한이 없습니다.");
        }

        comment.setContent(dto.getContent());
        comment.setIsModified(true);

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus delete(Long commentId) throws Exception {
        ClubArticleComment comment = clubArticleCommentRepository.findById(commentId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));

        if(!comment.getMember().getId().equals(SecurityUtil.getCurrentMemberId())){
            throw new Exception("권한이 없습니다.");
        }

        comment.setIsDeleted(true);
        return HttpStatus.OK;
    }
    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 멤버입니다."));
    }

    public ClubArticle findArticle(Long articleId){
        return clubArticleRepository.findById(articleId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 게시글입니다."));
    }

}
