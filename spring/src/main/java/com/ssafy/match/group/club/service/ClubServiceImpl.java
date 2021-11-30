package com.ssafy.match.group.club.service;

import com.ssafy.match.common.entity.City;
import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.file.repository.DBFileRepository;
import com.ssafy.match.group.clubboard.board.entity.ClubBoard;
import com.ssafy.match.group.clubboard.board.repository.ClubBoardRepository;
import com.ssafy.match.member.dto.MemberSimpleInfoResponseDto;
import com.ssafy.match.group.club.dto.request.ClubApplicationRequestDto;
import com.ssafy.match.group.club.dto.request.ClubCreateRequestDto;
import com.ssafy.match.group.club.dto.request.ClubUpdateRequestDto;
import com.ssafy.match.group.club.dto.response.ClubFormInfoResponseDto;
import com.ssafy.match.group.club.dto.response.ClubInfoResponseDto;
import com.ssafy.match.group.club.dto.response.InfoForApplyClubFormResponseDto;
import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.club.entity.ClubApplicationForm;
import com.ssafy.match.group.club.entity.CompositeMemberClub;
import com.ssafy.match.group.club.entity.MemberClub;
import com.ssafy.match.group.club.repository.ClubApplicationFormRepository;
import com.ssafy.match.group.club.repository.ClubRepository;
import com.ssafy.match.group.club.repository.MemberClubRepository;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.entity.MemberSns;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.member.repository.MemberSnsRepository;
import com.ssafy.match.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
// https://velog.io/@kdhyo/JavaTransactional-Annotation-%EC%95%8C%EA%B3%A0-%EC%93%B0%EC%9E%90-26her30h
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final MemberClubRepository memberClubRepository;
    private final ClubApplicationFormRepository clubApplicationFormRepository;
    private final DBFileRepository dbFileRepository;
    private final MemberSnsRepository memberSnsRepository;
    private final ClubBoardRepository clubBoardRepository;

    @Transactional
    public Long create(ClubCreateRequestDto dto) throws Exception {
        validCity(dto.getCity());

        Club club = new Club(dto);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        club.changeMember(member);
        club.setDbFile(findDBFile(dto.getUuid()));
        clubRepository.save(club);

        makeBasicBoards(club);
        addMember(club, member);

        return club.getId();
    }

    @Transactional
    public HttpStatus update(Long clubId, ClubUpdateRequestDto dto) throws Exception {
        validCity(dto.getCity());

        Club club = findClub(clubId);
        Long memberId = SecurityUtil.getCurrentMemberId();
        if (!club.getMember().getId().equals(memberId)) {
            throw new Exception("권한이 없습니다.");
        }

        club.update(dto);
        club.changeMember(findMember(dto.getHostId()));
        club.setDbFile(findDBFile(dto.getUuid()));

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus delete(Long clubId) throws Exception {
        Club club = findClub(clubId);

        if (!club.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new Exception("권한이 없습니다.");
        }

        List<MemberClub> memberClubs = memberClubRepository.findMemberRelationInClub(club);
        for (MemberClub mem : memberClubs) {
            mem.deActivation();
        }

        club.setIsActive(false);

        return HttpStatus.OK;
    }

    public Page<ClubInfoResponseDto> getAllClub(Pageable pageable) {
        Page<ClubInfoResponseDto> clubInfoResponseDtos = clubRepository.findByIsActiveAndIsPublic(Boolean.TRUE, Boolean.TRUE, pageable)
                .map(ClubInfoResponseDto::of);

        for (ClubInfoResponseDto clubInfoResponseDto: clubInfoResponseDtos.getContent()) {
            clubInfoResponseDto.setMemberSimpleInfoResponseDtos(makeMemberDtos(memberClubRepository.findMemberByClubId(clubInfoResponseDto.getId())));
        }
        return clubInfoResponseDtos;
    }

    // 현재 클럽 정보 리턴
    public ClubInfoResponseDto getOneClub(Long clubId) throws Exception {
        Club club = findClub(clubId);

        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())
            && !club.getIsPublic()) {
            throw new Exception("비공개된 클럽입니다.");
        }
        ClubInfoResponseDto clubInfoResponseDto = clubRepository.findById(clubId).map(ClubInfoResponseDto::of).orElseThrow(() -> new NullPointerException("클럽이 존재하지 않습니다"));
        clubInfoResponseDto.setMemberSimpleInfoResponseDtos(makeMemberDtos(memberClubRepository.findMemberByClubId(clubInfoResponseDto.getId())));
        return clubInfoResponseDto;
    }

    @Transactional
    public void addMember(Club club, Member member) {
        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);

        // DB에 해당 멤버 기록이 없다면 새로 생성
        MemberClub memberClub = memberClubRepository
            .findById(compositeMemberClub)
            .orElseGet(() -> MemberClub.builder()
                .compositeMemberClub(compositeMemberClub)
                .build());

        memberClub.activation();
        club.addMember();

        memberClubRepository.save(memberClub);
    }

    @Transactional
    public HttpStatus removeMember(Long clubId) throws Exception {
        Club club = findClub(clubId);
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new NullPointerException("잘못된 사용자 입니다.(사용자 없음)"));
        if (club.getMember().getId().equals(member.getId())) {
            throw new Exception("클럽장은 탈퇴할 수 없습니다.");
        }
        CompositeMemberClub compositeMemberClub = new CompositeMemberClub(member, club);
        MemberClub memberClub = memberClubRepository.findById(compositeMemberClub)
            .orElseThrow(() -> new NullPointerException("가입 기록이 없습니다."));
        memberClub.deActivation();
        club.removeMember();
        return HttpStatus.OK;
    }

    @Transactional
    public void makeBasicBoards(Club club){
        clubBoardRepository.save(new ClubBoard("공지사항", club));
        clubBoardRepository.save(new ClubBoard("게시판", club));
    }

    // 클럽 존재 유무 확인
    public Club findClub(Long clubId) throws Exception {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new NullPointerException("클럽 정보가 없습니다."));

        if (!club.getIsActive()) {
            throw new Exception("삭제된 클럽입니다.");
        }

        return club;
    }

    public Member findMember(Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NullPointerException("회원 정보가 없습니다."));

        if (!member.getIs_active()) {
            throw new Exception("삭제된 멤버입니다.");
        }

        return member;
    }

    // 생성 및 업데이트에 사용
    public DBFile findDBFile(String uuid) {
        if (uuid == null) {
            return null;
        }
        return dbFileRepository.findById(uuid)
            .orElseThrow(() -> new NullPointerException("파일 정보가 없습니다."));
    }

    // 현재 클럽에 속한 멤버 리스트
    public List<Member> findMemberInClub(Club club) {
        return memberClubRepository.findMemberInClub(club);
    }

    // 멤버의 id, name, nickname만 가져옴 (org.springframework.core.convert.ConverterNotFoundException 방지)
    public List<MemberSimpleInfoResponseDto> makeMemberDtos(List<Member> members) {
        List<MemberSimpleInfoResponseDto> memberSimpleInfoResponseDtos = new ArrayList<>();

        for (Member member : members) {
            memberSimpleInfoResponseDtos.add(MemberSimpleInfoResponseDto.from(member));
        }

        return memberSimpleInfoResponseDtos;
    }

    public void validCity(String city) throws Exception {
        if (!Stream.of(City.values()).map(Enum::name)
            .collect(Collectors.toList()).contains(city)) {
            throw new Exception("존재하지 않는 지역입니다");
        }
    }

    // 신청 버튼 클릭시 관련 정보 및 권한 체크
    public InfoForApplyClubFormResponseDto getInfoForApply(Long clubId) throws Exception {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Club club = findClub(clubId);

        List<Member> memberList = findMemberInClub(club);
        for (Member mem : memberList) {
            if (SecurityUtil.getCurrentMemberId().equals(mem.getId())) {
                throw new Exception("이미 가입한 멤버입니다.");
            }
        }

        InfoForApplyClubFormResponseDto dto = InfoForApplyClubFormResponseDto.builder()
            .nickname(member.getNickname())
            .city(member.getCity())
//            .experiencedTechstack(memberExperiencedTechstackRepository.findTechstackByMemberName(member))
//            .beginnerTechstack(memberBeginnerTechstackRepository.findTechstackByMemberName(member))
            .build();

        Optional<MemberSns> git = memberSnsRepository.findByMemberAndSnsName(member, "github");
        Optional<MemberSns> twitter = memberSnsRepository.findByMemberAndSnsName(member, "twitter");
        Optional<MemberSns> facebook = memberSnsRepository
            .findByMemberAndSnsName(member, "facebook");
        Optional<MemberSns> backjoon = memberSnsRepository
            .findByMemberAndSnsName(member, "backjoon");

        if (git.isPresent()) {
            dto.setGit(git.get().getSnsAccount());
        }
        if (twitter.isPresent()) {
            dto.setTwitter(twitter.get().getSnsAccount());
        }
        if (facebook.isPresent()) {
            dto.setFacebook(facebook.get().getSnsAccount());
        }
        if (backjoon.isPresent()) {
            dto.setBackjoon(backjoon.get().getSnsAccount());
        }

        return dto;
    }

    @Transactional
    public HttpStatus applyClub(Long clubId, ClubApplicationRequestDto dto) throws Exception {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Club club = findClub(clubId);

        CompositeMemberClub cmp = new CompositeMemberClub(member, club);

        Optional<ClubApplicationForm> form = clubApplicationFormRepository.findById(cmp);
        if (form.isPresent()) {
            throw new Exception("신청한 내역이 존재합니다.");
        }

        ClubApplicationForm clubApplicationForm = new ClubApplicationForm(cmp, dto);

        clubApplicationForm.setDbFile(findDBFile(dto.getUuid()));

        clubApplicationFormRepository.save(clubApplicationForm);
        return HttpStatus.OK;
    }

    // 모든 신청서 작성일 기준 내림차순 조회
    public List<ClubFormInfoResponseDto> getAllClubForm(Long clubId) throws Exception {
        Club club = findClub(clubId);

        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())) {
            throw new Exception("조회 권한이 없습니다.");
        }

        List<ClubApplicationForm> forms = clubApplicationFormRepository.formByClubId(club);
        List<ClubFormInfoResponseDto> clubFormInfoResponseDtos = new ArrayList<>();

        for (ClubApplicationForm form : forms) {
            clubFormInfoResponseDtos.add(ClubFormInfoResponseDto.builder()
                .form(form)
//                .experiencedTechstack(memberExperiencedTechstackRepository
//                    .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
//                .beginnerTechstack(memberBeginnerTechstackRepository
//                    .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
                .build());

        }

        return clubFormInfoResponseDtos;
    }

    // 닉네임으로 신청서 검색
    public List<ClubFormInfoResponseDto> getAllFormByClubNickname(Long clubId, String nickname)
        throws Exception {
        Club club = findClub(clubId);

        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())) {
            throw new Exception("조회 권한이 없습니다.");
        }

        List<ClubApplicationForm> forms = clubApplicationFormRepository
            .allFormByClubNickname(club, nickname);
        List<ClubFormInfoResponseDto> clubFormInfoResponseDtos = new ArrayList<>();

        for (ClubApplicationForm form : forms) {
            clubFormInfoResponseDtos.add(ClubFormInfoResponseDto.builder()
                .form(form)
//                .experiencedTechstack(memberExperiencedTechstackRepository
//                    .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
//                .beginnerTechstack(memberBeginnerTechstackRepository
//                    .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
                .build());
        }

        return clubFormInfoResponseDtos;
    }

    // 신청서 목록의 복합 기본키를 가져와 해당 신청서 상세조회
    public ClubFormInfoResponseDto getOneClubForm(Long clubId, Long memberId) throws Exception {
        CompositeMemberClub cms = new CompositeMemberClub(findMember(memberId),
            findClub(clubId));

        ClubApplicationForm form = clubApplicationFormRepository.oneFormById(cms)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 신청서입니다"));

        return ClubFormInfoResponseDto.builder()
            .form(form)
//            .experiencedTechstack(memberExperiencedTechstackRepository
//                .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
//            .beginnerTechstack(memberBeginnerTechstackRepository
//                .findTechstackByMemberName(form.getCompositeMemberClub().getMember()))
            .build();
    }

    // 가입 승인
    @Transactional
    public HttpStatus approval(Long clubId, Long memberId) throws Exception {
        Club club = findClub(clubId);
        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())) {
            throw new Exception("승인 권한이 없습니다");
        }
        List<Member> memberList = findMemberInClub(club);
        Member member = findMember(memberId);

        for (Member mem : memberList) {
            if (mem.getId().equals(memberId)) {
                throw new Exception("이미 가입되어있는 회원입니다.");
            }
        }

        addMember(club, member);
        reject(clubId, memberId);

        return HttpStatus.OK;
    }

    //     신청서 제거
    @Transactional
    public HttpStatus reject(Long clubId, Long memberId) throws Exception {
        Club club = findClub(clubId);

        if (!SecurityUtil.getCurrentMemberId().equals(club.getMember().getId())
            && !SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new Exception("승인 권한이 없습니다");
        }

        clubApplicationFormRepository.delete(clubApplicationFormRepository
            .findById(new CompositeMemberClub(findMember(memberId), club))
            .orElseThrow(() -> new NullPointerException("존재하지 않는 신청서입니다.")));

        return HttpStatus.OK;
    }

}
