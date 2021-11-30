package com.ssafy.match.group.projectboard.article.service;


import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.projectboard.article.dto.ProjectArticleInfoResponseDto;
import com.ssafy.match.group.projectboard.article.dto.ProjectArticleSimpleInfoResponseDto;
import com.ssafy.match.group.projectboard.article.dto.ProjectArticleRequestDto;
import com.ssafy.match.group.projectboard.article.entity.ProjectArticle;
import com.ssafy.match.group.projectboard.article.entity.ProjectArticleTag;
import com.ssafy.match.group.projectboard.article.entity.ProjectContent;
import com.ssafy.match.group.projectboard.article.repository.ProjectArticleRepository;
import com.ssafy.match.group.projectboard.article.repository.ProjectArticleTagRepository;
import com.ssafy.match.group.projectboard.article.repository.ProjectContentRepository;
import com.ssafy.match.group.projectboard.board.entity.ProjectBoard;
import com.ssafy.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.ssafy.match.group.projectboard.comment.entity.ProjectArticleComment;
import com.ssafy.match.group.projectboard.comment.repository.ProjectArticleCommentRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectArticleService {

    private final ProjectBoardRepository projectBoardRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectContentRepository projectContentRepository;
    private final MemberRepository memberRepository;
    private final ProjectArticleTagRepository projectArticleTagRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;

    @Transactional(readOnly = true)
    public ProjectArticleInfoResponseDto getProjectArticleDetail(Long articleId) {
        ProjectArticle projectArticle = findProjectArticle(articleId);
        ProjectArticleInfoResponseDto projectArticleInfoResponseDto = ProjectArticleInfoResponseDto.of(
            projectArticle, getProjectArticleTagList(projectArticle));
        ProjectContent projectContent = findProjectContent(projectArticle);
        projectArticleInfoResponseDto.setContent(projectContent.getContent());
        return projectArticleInfoResponseDto;
    }

    public List<String> getProjectArticleTagList(ProjectArticle projectArticle) {
        List<ProjectArticleTag> pats = projectArticleTagRepository.findAllByProjectArticle(
            projectArticle);
        List<String> tags = new ArrayList<>();
        for (ProjectArticleTag pat : pats) {
            tags.add(pat.getName());
        }
        return tags;
    }

    @Transactional(readOnly = true)
    public Page<ProjectArticleSimpleInfoResponseDto> getProjectArticles(Integer boardId,
        Pageable pageable) {
        Page<ProjectArticle> projectArticles = projectArticleRepository.findAllByProjectBoard(findProjectBoard(boardId), pageable);
        return projectArticles.map(m -> ProjectArticleSimpleInfoResponseDto.of(m, getProjectArticleTagList(m)));
    }

//    @Transactional(readOnly = true)
//    public Page<ProjectArticleSimpleInfoResponseDto> getProjectArticlesByTitle(Integer boardId, String title, Pageable pageable) throws Exception {
//        if (!projectBoardRepository.existsById(boardId)) {
//            throw new RuntimeException("존재하지 않는 게시판입니다");
//        }
//        ProjectBoard projectBoard = projectBoardRepository.getById(boardId);
//        Page<ProjectArticleSimpleInfoResponseDto> projectArticleListDtos = projectArticleRepository.findAllByProjectBoardAndTitle(projectBoard, title, pageable)
//            .map(ProjectArticleSimpleInfoResponseDto::of);
//        return projectArticleListDtos;
//    }

//    @Transactional(readOnly = true)
//    public Page<ProjectArticleSimpleInfoResponseDto> getProjectArticlesByNickname(Integer boardId, String nickname, Pageable pageable) throws Exception {
//        if (!projectBoardRepository.existsById(boardId)) {
//            throw new RuntimeException("존재하지 않는 게시판입니다");
//        }
//        ProjectBoard projectBoard = projectBoardRepository.getById(boardId);
//        Page<ProjectArticleSimpleInfoResponseDto> projectArticleListDtos = projectArticleRepository.findAllByProjectBoardAndNickname(projectBoard, nickname, pageable)
//            .map(ProjectArticleSimpleInfoResponseDto::of);
//        return projectArticleListDtos;
//    }

    @Transactional
    public ProjectArticleInfoResponseDto createArticle(ProjectArticleRequestDto dto) {
        ProjectBoard projectBoard = findProjectBoard(dto.getProjectBoardId());
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        ProjectArticle projectArticle = projectArticleRepository.save(
            ProjectArticle.of(dto, projectBoard, member));
        addContent(projectArticle, dto.getContent());
        addTags(projectArticle, dto.getTags());
        return ProjectArticleInfoResponseDto.of(projectArticle, dto.getTags());
    }

    @Transactional
    public ProjectArticleInfoResponseDto updateArticle(Long articleId,
        ProjectArticleRequestDto dto) {
        // 게시글
        ProjectArticle projectArticle = findProjectArticle(articleId);
        if (dto.getContent() == null) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
        // 게시글 내용
        ProjectContent projectContent = findProjectContent(projectArticle);
        projectContent.setContent(dto.getContent());

        projectArticle.update(dto, findProjectBoard(dto.getProjectBoardId()));
        addTags(projectArticle, dto.getTags());

        ProjectArticleInfoResponseDto projectArticleInfoResponseDto = ProjectArticleInfoResponseDto.of(
            projectArticle, dto.getTags());
        projectArticleInfoResponseDto.setContent(projectContent.getContent());
        return projectArticleInfoResponseDto;
    }

    @Transactional
    public HttpStatus deleteArticle(Long articleId) {
        ProjectArticle projectArticle = findProjectArticle(articleId);
        ProjectContent projectContent = findProjectContent(projectArticle);
        deleteAllByProjectArticle(projectArticle);
        projectArticleTagRepository.deleteAllTagsByProjectArticle(projectArticle);
        projectContentRepository.delete(projectContent);
        projectArticleRepository.delete(projectArticle);

        return HttpStatus.OK;
    }

    @Transactional
    public void addContent(ProjectArticle projectArticle, String content) {
        projectContentRepository.save(ProjectContent.of(projectArticle, content));
    }

    // 프로젝트 게시글 조회수 증가
    @Transactional
    public HttpStatus plusViewCount(Long projectArticleId) {
        findProjectArticle(projectArticleId).plusViewCount();
        return HttpStatus.OK;
    }

    public ProjectBoard findProjectBoard(int boardId) {
        return projectBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public ProjectArticle findProjectArticle(Long projectArticleId) {
        return projectArticleRepository.findById(projectArticleId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public ProjectContent findProjectContent(ProjectArticle projectArticle) {
        return projectContentRepository.getByProjectArticle(projectArticle)
            .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void deleteAllByProjectArticle(ProjectArticle projectArticle){
        projectArticleCommentRepository.deleteAllByProjectArticle(projectArticle);
    }

    // 게시글 태그 추가
    @Transactional
    public void addTags(ProjectArticle projectArticle, List<String> tags) {
        projectArticleTagRepository.deleteAllTagsByProjectArticle(projectArticle);
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            projectArticleTagRepository.save(ProjectArticleTag.of(projectArticle, tag));
        }

    }

}
