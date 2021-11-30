package com.ssafy.match.group.clubboard.board.dto;

import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ClubBoardInfoDto {
    @ApiModelProperty(name="id", example = "3")
    private Integer id;

    @ApiModelProperty(name = "name", example = "일정게시판")
    private String name;

    public static ClubBoardInfoDto of(ClubBoard clubBoard) {
        return ClubBoardInfoDto.builder()
                .id(clubBoard.getId())
                .name(clubBoard.getName())
                .build();
    }

    @Builder
    public ClubBoardInfoDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
