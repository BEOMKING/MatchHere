package com.ssafy.match.group.clubboard.article.entity;

import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.club_article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_board_id")
    private ClubBoard clubBoard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "view_count")
    private Integer viewCount;

    @Builder
    public ClubArticle(ClubBoard clubBoard, Member member, String title, LocalDateTime createDate, LocalDateTime modifiedDate, Integer viewCount) {
        this.clubBoard = clubBoard;
        this.member = member;
        this.title = title;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.viewCount = viewCount;
    }
}
