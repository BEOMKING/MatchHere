package com.ssafy.match.group.club.entity;

import com.ssafy.match.common.entity.City;
import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.group.club.dto.request.ClubCreateRequestDto;
import com.ssafy.match.group.club.dto.request.ClubUpdateRequestDto;
import com.ssafy.match.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "matching.club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String topic;
    private String bio;
    @Column(name = "member_count")
    private int memberCount;
    @Column(name = "max_count")
    private int maxCount;

    @Enumerated(EnumType.STRING)
    private City city;
    @Column(name = "create_Date")
    private LocalDateTime createDate;

    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_public")
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pic")
    private DBFile dbFile;

    public void addMember(){
        this.memberCount++;
    }

    public void removeMember(){
        this.memberCount--;
    }

    public void changeMember(Member member) throws Exception {
        if(member == null){
            throw new Exception("프로젝트장은 존재해야합니다.");
        }
        this.member = member;
    }

    public void setMaxCount(int num) throws Exception {
        if(num < 1) {
            throw new Exception("인원은 1명 이상이 되어야합니다.");
        }
        this.maxCount = num;
    }

    public void update(ClubUpdateRequestDto dto) throws Exception {
        this.name = dto.getName();
        this.topic = dto.getTopic();
        this.bio = dto.getBio();
        setMaxCount(dto.getMaxCount());
        this.city = City.from(dto.getCity());
        this.isPublic = dto.getIsPublic();
    }

    public Club(ClubCreateRequestDto dto) throws Exception {
        this.name = dto.getName();
        this.topic = dto.getTopic();
        this.bio = dto.getBio();
        this.memberCount = 0;
        setMaxCount(dto.getMaxCount());
        this.city = City.from(dto.getCity());
        this.createDate = LocalDateTime.now();
        this.isActive = true;
        this.isPublic = dto.getIsPublic();
    }

}