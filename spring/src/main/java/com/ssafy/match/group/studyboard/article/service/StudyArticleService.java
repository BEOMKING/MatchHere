package com.ssafy.match.group.studyboard.article.service;


import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.studyboard.article.dto.StudyArticleInfoResponseDto;
import com.ssafy.match.group.studyboard.article.dto.StudyArticleRequestDto;
import com.ssafy.match.group.studyboard.article.dto.StudyArticleSimpleInfoResponseDto;
import com.ssafy.match.group.studyboard.article.entity.StudyArticle;
import com.ssafy.match.group.studyboard.article.entity.StudyArticleTag;
import com.ssafy.match.group.studyboard.article.entity.StudyContent;
import com.ssafy.match.group.studyboard.article.repository.StudyArticleRepository;
import com.ssafy.match.group.studyboard.article.repository.StudyArticleTagRepository;
import com.ssafy.match.group.studyboard.article.repository.StudyContentRepository;
import com.ssafy.match.group.studyboard.board.entity.StudyBoard;
import com.ssafy.match.group.studyboard.board.repository.StudyBoardRepository;
import com.ssafy.match.group.studyboard.comment.repository.StudyArticleCommentRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyArticleService {
    private final StudyBoardRepository studyBoardRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyContentRepository studyContentRepository;
    private final MemberRepository memberRepository;
    private final StudyArticleTagRepository studyArticleTagRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;

    @Transactional(readOnly = true)
    public StudyArticleInfoResponseDto getStudyArticleDetail(Long articleId) {
        StudyArticle studyArticle = findStudyArticle(articleId);
        StudyArticleInfoResponseDto studyArticleInfoResponseDto = StudyArticleInfoResponseDto.of(
            studyArticle, getStudyArticleTagList(studyArticle));
        StudyContent studyContent = findStudyContent(studyArticle);
        studyArticleInfoResponseDto.setContent(studyContent.getContent());
        return studyArticleInfoResponseDto;
    }

    public List<String> getStudyArticleTagList(StudyArticle studyArticle) {
        List<StudyArticleTag> pats = studyArticleTagRepository.findAllByStudyArticle(
            studyArticle);
        List<String> tags = new ArrayList<>();
        for (StudyArticleTag pat : pats) {
            tags.add(pat.getName());
        }
        return tags;
    }

    @Transactional(readOnly = true)
    public Page<StudyArticleSimpleInfoResponseDto> getStudyArticles(Integer boardId,
        Pageable pageable) {
        Page<StudyArticle> studyArticles = studyArticleRepository.findAllByStudyBoard(findStudyBoard(boardId), pageable);
        return studyArticles.map(m -> StudyArticleSimpleInfoResponseDto.of(m, getStudyArticleTagList(m)));
    }

    @Transactional
    public StudyArticleInfoResponseDto createArticle(StudyArticleRequestDto dto) {
        StudyBoard studyBoard = findStudyBoard(dto.getStudyBoardId());
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        StudyArticle studyArticle = studyArticleRepository.save(
            StudyArticle.of(dto, studyBoard, member));
        addContent(studyArticle, dto.getContent());
        addTags(studyArticle, dto.getTags());
        return StudyArticleInfoResponseDto.of(studyArticle, dto.getTags());
    }

    @Transactional
    public StudyArticleInfoResponseDto updateArticle(Long articleId,
        StudyArticleRequestDto dto) {
        // 게시글
        StudyArticle studyArticle = findStudyArticle(articleId);
        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        // 게시글 내용
        StudyContent studyContent = findStudyContent(studyArticle);
        studyContent.setContent(dto.getContent());

        studyArticle.update(dto, findStudyBoard(dto.getStudyBoardId()));
        addTags(studyArticle, dto.getTags());

        StudyArticleInfoResponseDto studyArticleInfoResponseDto = StudyArticleInfoResponseDto.of(
            studyArticle, dto.getTags());
        studyArticleInfoResponseDto.setContent(studyContent.getContent());
        return studyArticleInfoResponseDto;
    }

    @Transactional
    public HttpStatus deleteArticle(Long articleId) {
        StudyArticle studyArticle = findStudyArticle(articleId);
        StudyContent studyContent = findStudyContent(studyArticle);
        deleteAllCommentByStudyArticle(studyArticle);
        studyArticleTagRepository.deleteAllTagsByStudyArticle(studyArticle);
        studyContentRepository.delete(studyContent);
        studyArticleRepository.delete(studyArticle);

        return HttpStatus.OK;
    }

    @Transactional
    public void addContent(StudyArticle studyArticle, String content) {
        studyContentRepository.save(StudyContent.of(studyArticle, content));
    }

    // 스터디 게시글 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long studyArticleId) {
        findStudyArticle(studyArticleId).plusViewCount();
        return HttpStatus.OK;
    }

    public StudyBoard findStudyBoard(int boardId) {
        return studyBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!member.getIs_active()){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    public StudyArticle findStudyArticle(Long studyArticleId) {
        return studyArticleRepository.findById(studyArticleId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyContent findStudyContent(StudyArticle studyArticle) {
        return studyContentRepository.getByStudyArticle(studyArticle)
            .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllCommentByStudyArticle(StudyArticle studyArticle){
        studyArticleCommentRepository.deleteAllByStudyArticle(studyArticle);
    }

    // 게시글 태그 추가
    @Transactional
    public void addTags(StudyArticle studyArticle, List<String> tags) {
        studyArticleTagRepository.deleteAllTagsByStudyArticle(studyArticle);
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            studyArticleTagRepository.save(StudyArticleTag.of(studyArticle, tag));
        }

    }

}
