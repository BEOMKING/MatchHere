package com.ssafy.match.group.clubboard.board.service;

import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.club.repository.ClubRepository;
import com.ssafy.match.group.clubboard.board.dto.ClubBoardCreateRequestDto;
import com.ssafy.match.group.clubboard.board.dto.ClubBoardInfoDto;
import com.ssafy.match.group.clubboard.board.dto.ClubBoardUpdateDto;
import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
import com.ssafy.match.group.clubboard.board.repository.ClubBoardRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubBoardService {
    private final MemberRepository memberRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final ClubRepository clubRepository;

    @Transactional(readOnly = true)
    public List<ClubBoardInfoDto> getClubBoards(Long clubId) throws Exception {
        if (!clubRepository.existsById(clubId)) {
            throw new RuntimeException("존재하지 않는 club입니다.");
        }
        Club club = clubRepository.getById(clubId);
        List<ClubBoardInfoDto> clubBoardInfoDtos = clubBoardRepository.findAllByClub(club).stream()
                .map(ClubBoardInfoDto::of)
                .collect(Collectors.toList());
        return clubBoardInfoDtos;
    }

    @Transactional
    public Integer createBoard(Long clubId, ClubBoardCreateRequestDto clubBoardCreateRequestDto) throws Exception {
        if (!clubRepository.existsById(clubId)) {
            throw new RuntimeException("존재하지 않는 club입니다.");
        }
        Club club = clubRepository.getById(clubId);
        ClubBoard clubBoard = clubBoardCreateRequestDto.toClubBoard(club);
        ClubBoard ret = clubBoardRepository.save(clubBoard);
        return ret.getId();
    }

    @Transactional
    public Boolean deleteBoard(Integer boardId) throws Exception {
        if (!clubBoardRepository.existsById(boardId)) {
            throw new RuntimeException("존재하지 않는 게시판입니다.");
        }
        clubBoardRepository.delete(clubBoardRepository.getById(boardId));
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateBoard(Integer boardId, ClubBoardUpdateDto clubBoardUpdateDto) throws Exception {
        if (!clubBoardRepository.existsById(boardId)) {
            throw new RuntimeException("존재하지 않는 게시판입니다.");
        }
        ClubBoard clubBoard = clubBoardRepository.getById(boardId);
        clubBoard.setName(clubBoardUpdateDto.getName());
        return Boolean.TRUE;
    }

    public Member findMember(Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("회원 정보가 없습니다."));
        if (!member.getIs_active()) {
            throw new Exception("삭제된 멤버입니다.");
        }
        return member;
    }
}
