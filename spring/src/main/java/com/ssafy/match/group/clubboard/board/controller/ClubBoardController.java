package com.ssafy.match.group.clubboard.board.controller;


import com.ssafy.match.group.clubboard.board.dto.ClubBoardCreateRequestDto;
import com.ssafy.match.group.clubboard.board.dto.ClubBoardInfoDto;
import com.ssafy.match.group.clubboard.board.dto.ClubBoardUpdateDto;
import com.ssafy.match.group.clubboard.board.service.ClubBoardService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubBoardController {
    private final ClubBoardService clubBoardService;

    @GetMapping("/{clubid}/boards")
    @ApiOperation(value = "(클럽)게시판 리스트 조회", notes = "<strong>받은 클럽 id</strong>를 사용해서 게시판들을 조회한다.")
    public ResponseEntity<List<ClubBoardInfoDto>> getBoards(@PathVariable("clubid") Long clubid) throws Exception {
        return ResponseEntity.ok(clubBoardService.getClubBoards(clubid));
    }

    @PostMapping("/{clubid}/boards")
    @ApiOperation(value = "(클럽)게시판 생성", notes = "<strong>받은 클럽 id</strong>를 사용해서 게시판을 생성한다.")
    public ResponseEntity<Integer> create(@PathVariable("clubid") Long clubid, @RequestBody ClubBoardCreateRequestDto clubBoardCreateRequestDto) throws Exception {
        return ResponseEntity.ok(clubBoardService.createBoard(clubid, clubBoardCreateRequestDto));
    }

    @DeleteMapping("/{clubid}/boards/{boardid}")
    @ApiOperation(value = "(클럽)게시판 삭제", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 삭제한다.")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("clubid") Long clubid, @PathVariable("boardid") Integer boardid) throws Exception {
        return ResponseEntity.ok(clubBoardService.deleteBoard(boardid));
    }

    @PutMapping("/{clubid}/boards/{boardid}")
    @ApiOperation(value = "(클럽)게시판 수정", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시판을 수정한다.")
    public ResponseEntity<Boolean> updateBoard(@PathVariable("clubid") Long clubid, @PathVariable("boardid") Integer boardid, @RequestBody ClubBoardUpdateDto clubBoardUpdateDto) throws Exception {
        return ResponseEntity.ok(clubBoardService.updateBoard(boardid, clubBoardUpdateDto));
    }
}
