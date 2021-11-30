package com.ssafy.match.member.dto.response;


import com.ssafy.match.file.dto.DBFileDto;
import com.ssafy.match.member.entity.Member;
import lombok.*;

@Data
public class MemberBasicinfoResponseDto {
    private DBFileDto cover_pic;
    private String nickname;
    private String name;
    private String city;
    private String bio;

    public static MemberBasicinfoResponseDto of(Member member) {
        return MemberBasicinfoResponseDto.builder()
                .cover_pic(DBFileDto.of(member.getCover_pic()))
                .nickname(member.getNickname())
                .name(member.getName())
                .city(member.getCity())
                .bio(member.getBio())
                .build();
    }

    @Builder
    public MemberBasicinfoResponseDto(DBFileDto cover_pic, String nickname, String name, String city, String bio) {
        this.cover_pic = cover_pic;
        this.nickname = nickname;
        this.name = name;
        this.city = city;
        this.bio = bio;
    }
}