package com.ssafy.match.group.club.dto.response;

import com.ssafy.match.group.club.entity.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubSimpleInfoResponseDto {

    private Long id;
    private String name;

    public static ClubSimpleInfoResponseDto from(Club club) {
        return ClubSimpleInfoResponseDto.builder()
            .id(club.getId())
            .name(club.getName())
            .build();
    }

    @Builder
    public ClubSimpleInfoResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
