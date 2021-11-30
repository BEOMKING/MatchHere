package com.ssafy.match.group.club.service;

import com.ssafy.match.group.club.dto.request.ClubApplicationRequestDto;
import com.ssafy.match.group.club.dto.request.ClubCreateRequestDto;
import com.ssafy.match.group.club.dto.request.ClubUpdateRequestDto;
import com.ssafy.match.group.club.dto.response.ClubFormInfoResponseDto;
import com.ssafy.match.group.club.dto.response.ClubInfoResponseDto;
import com.ssafy.match.group.club.dto.response.InfoForApplyClubFormResponseDto;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

public interface ClubService {

    Long create(ClubCreateRequestDto dto) throws Exception;

    HttpStatus update(Long clubId, ClubUpdateRequestDto dto) throws Exception;

    HttpStatus delete(Long clubId) throws Exception;

    Page<ClubInfoResponseDto> getAllClub(Pageable pageable);

    ClubInfoResponseDto getOneClub(Long clubId) throws Exception;

    HttpStatus removeMember(Long studyId) throws Exception;

    InfoForApplyClubFormResponseDto getInfoForApply(Long clubId) throws Exception;

    HttpStatus applyClub(Long clubId, ClubApplicationRequestDto dto) throws Exception;

    public List<ClubFormInfoResponseDto> getAllClubForm(Long clubId) throws Exception;

    List<ClubFormInfoResponseDto> getAllFormByClubNickname(Long clubId, String nickname) throws Exception;

    ClubFormInfoResponseDto getOneClubForm(Long clubId, Long memberId) throws Exception;

    HttpStatus approval(Long clubId, Long memberId) throws Exception;

    HttpStatus reject(Long clubId, Long memberId) throws Exception;

}
