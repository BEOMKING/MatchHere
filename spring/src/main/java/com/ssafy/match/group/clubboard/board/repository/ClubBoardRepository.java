package com.ssafy.match.group.clubboard.board.repository;


import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubBoardRepository extends JpaRepository<ClubBoard, Integer> {
    List<ClubBoard> findAllByClub(Club club);
}
