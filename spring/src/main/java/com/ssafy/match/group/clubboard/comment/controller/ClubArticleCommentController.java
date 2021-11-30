package com.ssafy.match.group.clubboard.comment.controller;

import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentRequestDto;
import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentResponseDto;
import com.ssafy.match.group.clubboard.comment.service.ClubCommentService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubcomment")
public class ClubArticleCommentController {

    private final ClubCommentService clubCommentService;

    @PostMapping("/{articleId}/{parentId}")
    @ApiOperation(value = "댓글 작성", notes = "<strong>부모 댓글이라면 parentId는 0 입력</strong>")
    public ResponseEntity<Long> create(@PathVariable("articleId") Long articleId,
        @PathVariable("parentId") Long parentId, @RequestBody ClubArticleCommentRequestDto dto)
        throws Exception {
        return ResponseEntity.ok(clubCommentService.create(articleId, parentId, dto));
    }

    @GetMapping("/{articleId}")
    @ApiOperation(value = "게시글 댓글 조회",
        notes = "<strong>받은 게시물 id</strong>로 게시물의 댓글 리스트를 받는다.")
    public ResponseEntity<List<ClubArticleCommentResponseDto>> getAllComment(@PathVariable("articleId") Long articleId) {
        return ResponseEntity.ok(clubCommentService.allComment(articleId));
    }

    @PatchMapping("/{commentId}")
    @ApiOperation(value = "댓글 수정", notes = "<strong>받은 댓글 정보</strong>를 사용해서 댓글을 수정한다.")
    public ResponseEntity<HttpStatus> update(@PathVariable("commentId") Long commentId,
        @RequestBody ClubArticleCommentRequestDto dto) throws Exception {
        return ResponseEntity.ok(clubCommentService.update(commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "댓글 삭제", notes = "<strong>받은 댓글 Id</strong>로 댓글을 삭제한다.")
    public ResponseEntity<HttpStatus> delete(@PathVariable("commentId") Long commentId)
        throws Exception {
        return ResponseEntity.ok(clubCommentService.delete(commentId));
    }

}
