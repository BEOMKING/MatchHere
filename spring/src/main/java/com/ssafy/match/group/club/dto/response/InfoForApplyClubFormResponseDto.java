package com.ssafy.match.group.club.dto.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InfoForApplyClubFormResponseDto {

//    private String name;

    @ApiModelProperty(name = "nickname", example = "4")
    private String nickname;

    private String city;

    @ApiModelProperty(name = "git", example = "github")
    private String git;

    @ApiModelProperty(name = "twitter", example = "twitter.com")
    private String twitter;

    @ApiModelProperty(name = "facebook", example = "facebook.com")
    private String facebook;

    @ApiModelProperty(name = "backjoon", example = "BEOMKING")
    private String backjoon;

    private List<String> experiencedTechstack;

    private List<String> beginnerTechstack;

    private String bio;

}
