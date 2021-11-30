package com.ssafy.match.group.clubboard.board.entity;


import com.ssafy.match.group.club.entity.Club;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.club_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @Builder
    public ClubBoard(String name, Club club) {
        this.name = name;
        this.club = club;
    }
}
