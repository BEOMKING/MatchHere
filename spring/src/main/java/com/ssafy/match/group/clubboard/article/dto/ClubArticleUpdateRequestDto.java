package com.ssafy.match.group.clubboard.article.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClubArticleUpdateRequestDto {

    @ApiModelProperty(name = "title", example = "게시물 제목")
    @ApiParam(value = "게시물 제목", required = true)
    private String title;

    @ApiModelProperty(name = "content", example = "내용~~~~")
    @ApiParam(value = "게시물내용")
    private String content;

}
