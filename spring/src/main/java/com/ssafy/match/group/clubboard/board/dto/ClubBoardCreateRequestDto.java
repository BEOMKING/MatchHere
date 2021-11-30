package com.ssafy.match.group.clubboard.board.dto;


import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClubBoardCreateRequestDto {

//    @ApiModelProperty(name = "clubId", example = "3")
//    @ApiParam(value = "스터디 id", required = true)
//    @NotEmpty
//    private Long clubId;

    @ApiModelProperty(name = "name", example = "일정게시판")
    @ApiParam(value = "게시판 이름", required = true)
    @NotEmpty
    private String name;

    public ClubBoard toClubBoard(Club club) {
        return ClubBoard.builder()
                .name(name)
                .club(club)
                .build();
    }
}
