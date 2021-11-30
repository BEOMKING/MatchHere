package com.ssafy.match.group.club.repository;

import com.ssafy.match.group.club.entity.Club;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("select c from matching.club c where c.isActive = true and c.isPublic = true order by c.createDate desc ")
    List<Club> findAllClub();

    Page<Club> findByIsActiveAndIsPublic(Boolean isActive, Boolean isPublic, Pageable pageable);
}