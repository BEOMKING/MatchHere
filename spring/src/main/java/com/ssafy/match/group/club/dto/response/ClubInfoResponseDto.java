package com.ssafy.match.group.club.dto.response;

import com.ssafy.match.member.dto.MemberSimpleInfoResponseDto;
import com.ssafy.match.group.club.entity.Club;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "클럽 수정 정보", description = "클럽 수정을 위한 정보 Response Dto Class")
@Getter
@Setter
public class ClubInfoResponseDto {

    @ApiModelProperty(name = "id", example = "4")
    private Long id;

    @ApiModelProperty(name = "name", example = "알고리즘 클럽")
    private String name;

    @ApiModelProperty(name = "memberCount", example = "3")
    private int memberCount;

    @ApiModelProperty(name = "maxCount", example = "3")
    private int maxCount;

    @ApiModelProperty(name = "isPublic", example = "false")
    private Boolean isPublic;

    @ApiModelProperty(name = "city", example = "구미")
    private String city;

    @ApiModelProperty(name = "cover_pic", example = "커버사진 uri")
    private String cover_pic;

    @ApiModelProperty(name = "bio", example = "매칭 클럽입니다.")
    private String bio;

    @ApiModelProperty(name = "host", example = "{\"id\": 3, \"name\": \"박범진\", \"nickname\": \"BJP\"}")
    private MemberSimpleInfoResponseDto host;

    @ApiModelProperty(name = "memberDtos", example = "[{\"id\": 3, \"name\": \"문일민\", \"nickname\": \"별명\"}, {\"id\": 4, \"name\": \"박범진\", \"nickname\": \"내별명\"}]")
    private List<MemberSimpleInfoResponseDto> memberSimpleInfoResponseDtos;

    public static ClubInfoResponseDto of(Club club) {
        return ClubInfoResponseDto.builder()
                .id(club.getId())
                .name(club.getName())
                .memberCount(club.getMemberCount())
                .maxCount(club.getMaxCount())
                .isPublic(club.getIsPublic())
                .city(club.getCity().name())
                .cover_pic((club.getDbFile() == null) ? null : club.getDbFile().getDownload_uri())
                .bio(club.getBio())
                .host(MemberSimpleInfoResponseDto.from(club.getMember()))
                .build();
    }

    @Builder
    public ClubInfoResponseDto(Long id, String name, int memberCount, int maxCount, Boolean isPublic, String city, String cover_pic, String bio, MemberSimpleInfoResponseDto host) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.maxCount = maxCount;
        this.isPublic = isPublic;
        this.city = city;
        this.cover_pic = cover_pic;
        this.bio = bio;
        this.host = host;
    }
}
