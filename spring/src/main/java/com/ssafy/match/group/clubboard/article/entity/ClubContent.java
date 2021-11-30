package com.ssafy.match.group.clubboard.article.entity;


import com.ssafy.match.group.clubboard.article.entity.ClubArticle;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.club_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "club_article_id")
    private ClubArticle clubArticle;

    @Column(name = "content")
    private String content;

    @Builder
    public ClubContent(ClubArticle clubArticle, String content) {
        this.clubArticle = clubArticle;
        this.content = content;
    }

}
