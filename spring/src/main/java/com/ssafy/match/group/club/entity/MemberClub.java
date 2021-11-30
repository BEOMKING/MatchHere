package com.ssafy.match.group.club.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.member_club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberClub {

    @EmbeddedId
    private CompositeMemberClub compositeMemberClub;

    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    public void activation() {
        this.isActive = true;
    }

    public void deActivation() {
        this.isActive = false;
    }

    @Builder
    public MemberClub(CompositeMemberClub compositeMemberClub) {
        this.compositeMemberClub = compositeMemberClub;
        this.registerDate = LocalDateTime.now();
    }
}