package com.ssafy.match.group.projectboard.board.service;

import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.group.projectboard.article.entity.ProjectArticle;
import com.ssafy.match.group.projectboard.article.entity.ProjectContent;
import com.ssafy.match.group.projectboard.article.repository.ProjectArticleRepository;
import com.ssafy.match.group.projectboard.article.repository.ProjectArticleTagRepository;
import com.ssafy.match.group.projectboard.article.repository.ProjectContentRepository;
import com.ssafy.match.group.projectboard.board.dto.ProjectBoardCreateRequestDto;
import com.ssafy.match.group.projectboard.board.dto.ProjectBoardInfoDto;
import com.ssafy.match.group.projectboard.board.dto.ProjectBoardUpdateDto;
import com.ssafy.match.group.projectboard.board.entity.ProjectBoard;
import com.ssafy.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.ssafy.match.group.project.entity.Project;
import com.ssafy.match.group.project.repository.ProjectRepository;
import com.ssafy.match.group.projectboard.comment.repository.ProjectArticleCommentRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectBoardService {

    private final ProjectBoardRepository projectBoardRepository;
    private final ProjectRepository projectRepository;
    private final ProjectArticleRepository projectArticleRepository;
    private final ProjectContentRepository projectContentRepository;
    private final ProjectArticleTagRepository projectArticleTagRepository;
    private final ProjectArticleCommentRepository projectArticleCommentRepository;

    @Transactional(readOnly = true)
    public List<ProjectBoardInfoDto> getProjectBoards(Long projectId) {
        return projectBoardRepository.findAllByProject(findProject(projectId)).stream()
            .map(ProjectBoardInfoDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public ProjectBoardInfoDto createBoard(Long projectId, ProjectBoardCreateRequestDto dto) {
        return ProjectBoardInfoDto.from(projectBoardRepository.save(ProjectBoard.of(dto, findProject(projectId))));
    }

    @Transactional
    public HttpStatus deleteBoard(Integer boardId) {
        List<ProjectArticle> projectArticles = projectArticleRepository.findAllByProjectBoard(findBoard(boardId));
        for (ProjectArticle projectArticle: projectArticles) {
            ProjectContent projectContent = findProjectContent(projectArticle);
            projectContentRepository.delete(projectContent);
            deleteAllByProjectArticle(projectArticle);
            projectArticleTagRepository.deleteAllTagsByProjectArticle(projectArticle);
            projectArticleRepository.delete(projectArticle);
        }

        projectBoardRepository.delete(projectBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)));
        return HttpStatus.OK;
    }

    @Transactional
    public ProjectBoardInfoDto updateBoard(Integer boardId, ProjectBoardUpdateDto projectBoardUpdateDto) {
        ProjectBoard projectBoard = findBoard(boardId);
        projectBoard.setName(projectBoardUpdateDto.getName());
        return ProjectBoardInfoDto.from(projectBoard);
    }

    // 프로젝트 찾기
    public Project findProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (Boolean.FALSE.equals(project.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_PROJECT);
        }

        return project;
    }

    // 게시판 찾기
    public ProjectBoard findBoard(int boardId) {
        return projectBoardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
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
}
