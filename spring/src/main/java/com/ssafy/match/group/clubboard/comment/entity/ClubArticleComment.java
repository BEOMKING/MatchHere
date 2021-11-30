package com.ssafy.match.group.clubboard.comment.entity;

import com.ssafy.match.group.clubboard.article.entity.ClubArticle;
import com.ssafy.match.group.clubboard.comment.dto.ClubArticleCommentRequestDto;
import com.ssafy.match.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "club_article_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
    private int depth;

    @Column(name = "parent_id")
    private Long parentId;

//    @Column(name = "group_id") 뎁스가 1이 아니고 더 깊어진다면 필요
//    private Long groupId;

    @Column(name = "reply_count")
    private int replyCount;

    @Column(name = "is_modified")
    private Boolean isModified;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_article_id")
    private ClubArticle clubArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void addReplyCount(){
        replyCount++;
    }

    public void setDepth(Long parentId){
        if(parentId > 0){
            this.depth = 1;
        }else{
            this.depth = 0;
        }
        this.parentId = parentId;
    }

    @Builder
    public ClubArticleComment(Long parentId, ClubArticleCommentRequestDto clubArticleCommentRequestDto, Member member, ClubArticle clubArticle) {
        this.member = member;
        this.clubArticle = clubArticle;
        this.content = clubArticleCommentRequestDto.getContent();
        this.createDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
        this.isModified = false;
        this.isDeleted = false;
        setDepth(parentId);
        this.replyCount = 0;
    }
}
