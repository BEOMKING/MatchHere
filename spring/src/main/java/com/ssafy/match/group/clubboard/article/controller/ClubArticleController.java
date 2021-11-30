package com.ssafy.match.group.clubboard.article.controller;


import com.ssafy.match.group.clubboard.article.dto.ClubArticleCreateRequestDto;
import com.ssafy.match.group.clubboard.article.dto.ClubArticleInfoDto;
import com.ssafy.match.group.clubboard.article.dto.ClubArticleListDto;
import com.ssafy.match.group.clubboard.article.dto.ClubArticleUpdateRequestDto;
import com.ssafy.match.group.clubboard.article.service.ClubArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
@RequestMapping("/clubboards")
public class ClubArticleController {
    private final ClubArticleService clubArticleService;

    @GetMapping("/{boardid}/articles")
    @ApiOperation(value = "(클럽)게시물 리스트 조회", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시물들을 조회한다.")
    public ResponseEntity<Page<ClubArticleListDto>> getArticles(@PathVariable("boardid") Integer boardid, @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction= Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.ok(clubArticleService.getClubArticles(boardid, pageable));
    }

//    @GetMapping("/{boardid}/title/articles/{title}")
//    @ApiOperation(value = "(클럽)게시물 리스트 제목 조회", notes = "<strong>받은 게시판 id와 제목</strong>을 사용해서 게시물들을 조회한다.")
//    public ResponseEntity<Page<ClubArticleListDto>> getArticlesByTitle(@PathVariable("boardid") Integer boardid, @PathVariable("title") String title, @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction= Sort.Direction.DESC) Pageable pageable) throws Exception {
//        return ResponseEntity.ok(clubArticleService.getClubArticlesByTitle(boardid, title, pageable));
//    }

    @GetMapping("/{boardid}/articles/title/{title}")
    @ApiOperation(value = "(클럽)게시물 리스트 제목 조회", notes = "<strong>받은 게시판 id와 제목</strong>을 사용해서 게시물들을 조회한다.")
    public ResponseEntity<Page<ClubArticleListDto>> getArticlesByTitle(@PathVariable("boardid") Integer boardid, @PathVariable("title") String title, @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction= Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.ok(clubArticleService.getClubArticlesByTitle(boardid, title, pageable));
    }

    @GetMapping("/{boardid}/articles/nickname/{nickname}")
    @ApiOperation(value = "(클럽)게시물 리스트 닉네임 조회", notes = "<strong>받은 게시판 id와 닉네임</strong>을 사용해서 게시물들을 조회한다.")
    public ResponseEntity<Page<ClubArticleListDto>> getArticlesByNickname(@PathVariable("boardid") Integer boardid, @PathVariable("nickname") String nickname, @PageableDefault(size = 10) @SortDefault(sort = "createDate", direction= Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.ok(clubArticleService.getClubArticlesByNickname(boardid, nickname, pageable));
    }

    @GetMapping("/{boardid}/articles/{articleid}")
    @ApiOperation(value = "(클럽)게시물 상세조회", notes = "<strong>받은 article id</strong>를 사용해서 게시물 상세 조회")
    public ResponseEntity<ClubArticleInfoDto> getArticleDetail(@PathVariable("boardid") Integer boardid, @PathVariable("articleid") Long articleid) throws Exception {
        return ResponseEntity.ok(clubArticleService.getClubArticleDetail(articleid));
    }

    @PostMapping("/{boardid}/articles")
    @ApiOperation(value = "(클럽)게시판의 게시물 생성", notes = "<strong>받은 게시판 id</strong>를 사용해서 게시물을 생성한다.")
    public ResponseEntity<Long> createArticle(@PathVariable("boardid") Integer boardid, @RequestBody ClubArticleCreateRequestDto clubArticleCreateRequestDto) throws Exception {
        return ResponseEntity.ok(clubArticleService.createArticle(boardid, clubArticleCreateRequestDto));
    }

    @PutMapping("/{boardid}/articles/{articleid}")
    @ApiOperation(value = "(클럽)게시판의 게시물 수정", notes = "<strong>받은 게시물 id</strong>를 사용해서 게시물을 수정한다.")
    public ResponseEntity<Long> updateArticle(@PathVariable("boardid") Integer boardid, @PathVariable("articleid") Long articleid, @RequestBody ClubArticleUpdateRequestDto clubArticleUpdateRequestDto) throws Exception {
        return ResponseEntity.ok(clubArticleService.updateArticle(articleid, clubArticleUpdateRequestDto));
    }

    @DeleteMapping("/{boardid}/articles/{articleid}")
    @ApiOperation(value = "(클럽)게시판의 게시물 삭제", notes = "<strong>받은 게시물 id</strong>를 사용해서 게시물을 삭제한다.")
    public ResponseEntity<Boolean> deleteArticle(@PathVariable("boardid") Integer boardid, @PathVariable("articleid") Long articleid) {
        return ResponseEntity.ok(clubArticleService.deleteArticle(articleid));
    }
}
