package com.ssafy.match.group.club.dto.response;

import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.group.club.entity.ClubApplicationForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "클럽 신청서 조회 정보", description = "클럽 신청서 조회 정보 Response Dto Class")
public class ClubFormInfoResponseDto {

    @ApiModelProperty(name = "clubId", example = "4")
    @ApiParam(value = "클럽 id (가입 승인 요청을 위해)", required = true)
    private Long clubId;
    @ApiModelProperty(name = "memberId", example = "4")
    @ApiParam(value = "멤버 id (가입 승인 요청을 위해)", required = true)
    private Long memberId;
    @ApiModelProperty(name = "nickname", example = "4")
    @ApiParam(value = "신청 유저의 닉네임", required = true)
    private String nickname;
    @ApiModelProperty(name = "city", example = "구미")
    @ApiParam(value = "지역", required = true)
    private String city;
    @ApiModelProperty(name = "git", example = "github")
    @ApiParam(value = "Git 닉네임", required = true)
    private String git;
    @ApiModelProperty(name = "twitter", example = "twitter.com")
    @ApiParam(value = "Twitter 주소", required = true)
    private String twitter;
    @ApiModelProperty(name = "facebook", example = "facebook.com")
    @ApiParam(value = "Facebook 주소", required = true)
    private String facebook;
    @ApiModelProperty(name = "backjoon", example = "BEOMKING")
    @ApiParam(value = "백준 닉네임", required = true)
    private String backjoon;
    private List<String> experiencedTechstack;

    private List<String> beginnerTechstack;
    private String bio;

    @ApiModelProperty(name = "fileDownloadUri", example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    private String fileDownloadUri;

    public void setFileDownloadUri(DBFile dbFile){
        if(dbFile == null) return;
        this.fileDownloadUri = dbFile.getDownload_uri();
    }

    @Builder
    public ClubFormInfoResponseDto(ClubApplicationForm form, List<String> experiencedTechstack, List<String> beginnerTechstack){
        this.clubId = form.getCompositeMemberClub().getClub().getId();
        this.memberId = form.getCompositeMemberClub().getMember().getId();
        this.nickname = form.getNickname();
        this.city = form.getCity().name();
        this.git = form.getGit();
        this.twitter = form.getTwitter();
        this.facebook = form.getFacebook();
        this.backjoon = form.getBackjoon();
        this.experiencedTechstack = experiencedTechstack;
        this.beginnerTechstack = beginnerTechstack;
        this.bio = form.getBio();
        setFileDownloadUri(form.getDbFile());
    }
}
