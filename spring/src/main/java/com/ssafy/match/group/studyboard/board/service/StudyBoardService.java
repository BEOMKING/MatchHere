package com.ssafy.match.group.studyboard.board.service;

import com.ssafy.match.common.entity.GroupAuthority;
import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.study.entity.CompositeMemberStudy;
import com.ssafy.match.group.study.entity.MemberStudy;
import com.ssafy.match.group.study.entity.Study;
import com.ssafy.match.group.study.repository.MemberStudyRepository;
import com.ssafy.match.group.study.repository.StudyRepository;
import com.ssafy.match.group.studyboard.article.entity.StudyArticle;
import com.ssafy.match.group.studyboard.article.entity.StudyContent;
import com.ssafy.match.group.studyboard.article.repository.StudyArticleRepository;
import com.ssafy.match.group.studyboard.article.repository.StudyArticleTagRepository;
import com.ssafy.match.group.studyboard.article.repository.StudyContentRepository;
import com.ssafy.match.group.studyboard.board.dto.StudyBoardCreateRequestDto;
import com.ssafy.match.group.studyboard.board.dto.StudyBoardInfoDto;
import com.ssafy.match.group.studyboard.board.dto.StudyBoardUpdateDto;
import com.ssafy.match.group.studyboard.board.entity.StudyBoard;
import com.ssafy.match.group.studyboard.board.repository.StudyBoardRepository;
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
@RequiredArgsConstructor
public class StudyBoardService {

    private final StudyBoardRepository studyBoardRepository;
    private final StudyRepository studyRepository;
    private final StudyArticleRepository studyArticleRepository;
    private final StudyContentRepository studyContentRepository;
    private final StudyArticleTagRepository studyArticleTagRepository;
    private final StudyArticleCommentRepository studyArticleCommentRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional(readOnly = true)
    public List<StudyBoardInfoDto> getStudyBoards(Long studyId) {
        return studyBoardRepository.findAllByStudy(findStudy(studyId))
            .stream()
            .map(StudyBoardInfoDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public StudyBoardInfoDto createBoard(Long studyId, StudyBoardCreateRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);

        checkAuthority(study, member);
        return StudyBoardInfoDto.from(
            studyBoardRepository.save(StudyBoard.of(dto, study)));
    }

    @Transactional // 권한 확인 로직 필요
    public HttpStatus deleteBoard(Integer boardId) {
        List<StudyArticle> studyArticles = studyArticleRepository.findAllByStudyBoard(
            findBoard(boardId));
        for (StudyArticle studyArticle : studyArticles) {
            StudyContent studyContent = findStudyContent(studyArticle);
            studyContentRepository.delete(studyContent);
            deleteAllByStudyArticle(studyArticle);
            studyArticleTagRepository.deleteAllTagsByStudyArticle(studyArticle);
            studyArticleRepository.delete(studyArticle);
        }

        studyBoardRepository.delete(studyBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)));
        return HttpStatus.OK;
    }

    @Transactional  // 권한 확인 로직 필요
    public StudyBoardInfoDto updateBoard(Integer boardId, StudyBoardUpdateDto studyBoardUpdateDto) {
        StudyBoard studyBoard = findBoard(boardId);
        studyBoard.setName(studyBoardUpdateDto.getName());
        return StudyBoardInfoDto.from(studyBoard);
    }

    // 스터디 찾기
    public Study findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (Boolean.FALSE.equals(study.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_STUDY);
        }
        return study;
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getIs_active()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    // 게시판 찾기
    public StudyBoard findBoard(int boardId) {
        return studyBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public StudyArticle findStudyArticle(Long studyArticleId) {
        return studyArticleRepository.findById(studyArticleId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public StudyContent findStudyContent(StudyArticle studyArticle) {
        return studyContentRepository.getByStudyArticle(studyArticle)
            .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllByStudyArticle(StudyArticle studyArticle) {
        studyArticleCommentRepository.deleteAllByStudyArticle(studyArticle);
    }

    public void checkAuthority(Study study, Member member){
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!memberStudy.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        if (memberStudy.getAuthority().equals(GroupAuthority.팀원)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }
}
