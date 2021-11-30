package com.ssafy.match.group.clubboard.article.dto;

import com.ssafy.match.group.clubboard.article.entity.ClubArticle;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
public class ClubArticleListDto {
    @ApiModelProperty(name = "articleId", example = "3")
    private Long articleId;

    @ApiModelProperty(name = "title", example = "게시물 제목")
    private String title;

    @ApiModelProperty(name = "clubBoard", example = "일정게시판")
    private String clubBoard;

    @ApiModelProperty(name = "createdMember", example = "nickname")
    private String createdMember;

    @ApiModelProperty(name = "createdDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime createdDate;

    @ApiModelProperty(name = "modifiedDate", example = "2021-10-01T13:09:53.46748")
    private LocalDateTime modifiedDate;

    @ApiModelProperty(name = "viewCount", example = "2")
    private Integer viewCount;

    public static ClubArticleListDto of(ClubArticle clubArticle) {
        return ClubArticleListDto.builder()
                .articleId(clubArticle.getId())
                .title(clubArticle.getTitle())
                .clubBoard(clubArticle.getClubBoard().getName())
                .createdMember(clubArticle.getMember().getNickname())
                .createdDate(clubArticle.getCreateDate())
                .modifiedDate(clubArticle.getModifiedDate())
                .viewCount(clubArticle.getViewCount())
                .build();
    }

    @Builder
    public ClubArticleListDto(Long articleId, String title, String clubBoard, String createdMember, LocalDateTime createdDate, LocalDateTime modifiedDate, Integer viewCount) {
        this.articleId = articleId;
        this.title = title;
        this.clubBoard = clubBoard;
        this.createdMember = createdMember;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.viewCount = viewCount;

    }
}
