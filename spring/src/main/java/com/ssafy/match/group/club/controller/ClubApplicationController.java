package com.ssafy.match.group.club.controller;

import com.ssafy.match.group.club.dto.request.ClubApplicationRequestDto;
import com.ssafy.match.group.club.dto.response.InfoForApplyClubFormResponseDto;
import com.ssafy.match.group.club.dto.response.ClubFormInfoResponseDto;
import com.ssafy.match.group.club.service.ClubService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubapplication")
public class ClubApplicationController {

    private final ClubService clubService;

    @GetMapping("/infoforcreate/{clubId}")
    @ApiOperation(value = "신청서 생성을 위한 정보", notes = "<strong>클럽에 가입하기 위한</strong>신청서를 작성하기 위한 정보를 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<InfoForApplyClubFormResponseDto> getInfoForApply(@PathVariable("clubId") Long clubId) throws Exception {
        return ResponseEntity.ok(clubService.getInfoForApply(clubId));
    }

    @PostMapping("/{clubId}")
    @ApiOperation(value = "클럽 가입 신청", notes = "<strong>받은 신청서 정보로</strong>를 사용해서 클럽에 신청을 한다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<HttpStatus> applyClub(@PathVariable("clubId") Long clubId, @RequestBody ClubApplicationRequestDto dto) throws Exception {
        return ResponseEntity.ok(clubService.applyClub(clubId, dto));
    }

    @PostMapping("/approval/{clubId}/{memberId}")
    @ApiOperation(value = "클럽 가입 승인", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 멤버를 가입 승인한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<HttpStatus> approval(@PathVariable("clubId") Long clubId, @PathVariable("memberId") Long memberId) throws Exception {
        return ResponseEntity.ok(clubService.approval(clubId, memberId));
    }

    @DeleteMapping("{clubId}/{memberId}")
    @ApiOperation(value = "신청서 삭제", notes = "<strong>받은 신청서 Id</strong>를 사용해서 해당 신청서를 제거한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<HttpStatus> reject(@PathVariable("clubId") Long clubId, @PathVariable("memberId") Long memberId) throws Exception{
        return ResponseEntity.ok(clubService.reject(clubId, memberId));
    }

    @GetMapping("/all/{clubId}/{nickname}")
    @ApiOperation(value = "특정 클럽 신청서 닉네임 포함 모든 신청서 조회", notes = "특정 클럽의 닉네임 포함 모든 신청서 리스트를 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<List<ClubFormInfoResponseDto>> getAllFormByClubNickname(@PathVariable("clubId") Long clubId,
        @PathVariable("nickname") String nickname) throws Exception {
        return ResponseEntity.ok(clubService.getAllFormByClubNickname(clubId, nickname));
    }

    @GetMapping("/all/{clubId}")
    @ApiOperation(value = "특정 클럽 모든 신청서 조회", notes = "특정 클럽의 모든 신청서 리스트를 작성일 기준 내림차순으로 받는다")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<List<ClubFormInfoResponseDto>> getAllClubForm(@PathVariable("clubId") Long clubId) throws Exception {
        return ResponseEntity.ok(clubService.getAllClubForm(clubId));
    }

    @GetMapping("/one/{clubId}/{memberId}")
    @ApiOperation(value = "특정 신청서 조회", notes = "특정 신청서를 상세 조회한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공"),
    })
    public ResponseEntity<ClubFormInfoResponseDto> getOneClubForm(@PathVariable("clubId") Long clubId, @PathVariable("memberId") Long memberId) throws Exception {
        return ResponseEntity.ok(clubService.getOneClubForm(clubId, memberId));
    }

}
