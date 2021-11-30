package com.ssafy.match.common.repository;

import com.ssafy.match.common.dto.DetailPositionInterface;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.common.entity.DetailPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetailPositionRepository extends JpaRepository<DetailPosition, Integer> {
    @Query("select md from matching.detail_position md where md.member = :member")
    List<DetailPositionInterface> findAllByMemberWithInterface(@Param("member") Member member);

    List<DetailPosition> findAllByMember(Member member);

    boolean existsByMemberAndName(Member member, String name);

    Optional<DetailPosition> findByMemberAndName(Member member, String name);
}