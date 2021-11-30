package com.ssafy.match.group.clubboard.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubArticleCommentResponseDto {

    @ApiModelProperty(name = "id", notes = "댓글의 id")
    private Long id;

    @ApiModelProperty(name = "nickname", notes = "댓글 작성자 닉네임")
    private String nickname;

    @ApiModelProperty(name = "content", notes = "댓글 내용")
    private String content;

    @ApiModelProperty(name = "isModified", notes = "수정 여부(수정되었으면 댓글에 수정됨 표시)")
    private Boolean isModified;

    @ApiModelProperty(name = "isDeleted", notes = "삭제 여부(삭제되었으면 삭제된 메세지입니다 표시)")
    private Boolean isDeleted;

    @ApiModelProperty(name = "parentId", notes = "부모의 id(id끼리 묶을 수 있음)")
    private Long parentId;

    @ApiModelProperty(name = "depth", notes = "댓글의 뎁스 표시(1이면 대댓글을 의미)")
    private int depth;

    @ApiModelProperty(name = "replyCount", notes = "대댓글 수")
    private int replyCount;

    @Builder
    public ClubArticleCommentResponseDto(Long id, String nickname, String content,
        Boolean isModified, Boolean isDeleted, Long parentId, int depth, int replyCount) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.isModified = isModified;
        this.isDeleted = isDeleted;
        this.parentId = parentId;
        this.depth = depth;
        this.replyCount = replyCount;
    }
}
