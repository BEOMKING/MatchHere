package com.ssafy.match.group.club.dto.request;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubApplicationRequestDto {

    @ApiModelProperty(name = "nickname", example = "BJP")
    private String nickname;

    @ApiModelProperty(name = "city", example = "서울")
    private String city;

    @ApiModelProperty(name = "git", example = "BEOMKING")
    private String git;

    @ApiModelProperty(name = "twitter", example = "twitter.com")
    private String twitter;

    @ApiModelProperty(name = "facebook", example = "facebook.com")
    private String facebook;

    @ApiModelProperty(name = "backjoon", example = "qjawlsqjacks")
    private String backjoon;

    @ApiModelProperty(name = "bio", example = "설명ㅇㅇㅇㅇ")
    private String bio;

    @ApiModelProperty(name = "experiencedTechstack", example = "[\"java\", \"python\"]")
    private List<String> experiencedTechstack;

    @ApiModelProperty(name = "beginnerTechstack", example = "[\"java\", \"python\"]")
    private List<String> beginnerTechstack;

    @ApiModelProperty(name = "uuid", example = "3fads23-fdfd13-23d2")
    private String uuid;

}