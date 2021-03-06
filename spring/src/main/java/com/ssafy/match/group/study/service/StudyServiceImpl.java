package com.ssafy.match.group.study.service;

import com.ssafy.match.common.entity.GroupAuthority;
import com.ssafy.match.common.entity.GroupCity;
import com.ssafy.match.common.entity.PublicScope;
import com.ssafy.match.common.entity.RecruitmentState;
import com.ssafy.match.common.entity.StudyProgressState;
import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.file.dto.DBFileDto;
import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.file.repository.DBFileRepository;
import com.ssafy.match.group.club.dto.response.ClubSimpleInfoResponseDto;
import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.club.repository.ClubRepository;
import com.ssafy.match.group.club.repository.MemberClubRepository;
import com.ssafy.match.group.study.dto.request.StudyApplicationRequestDto;
import com.ssafy.match.group.study.dto.request.StudyCreateRequestDto;
import com.ssafy.match.group.study.dto.request.StudyUpdateRequestDto;
import com.ssafy.match.group.study.dto.response.StudyFormInfoResponseDto;
import com.ssafy.match.group.study.dto.response.StudyFormSimpleInfoResponseDto;
import com.ssafy.match.group.study.dto.response.StudyInfoForCreateResponseDto;
import com.ssafy.match.group.study.dto.response.StudyInfoForUpdateResponseDto;
import com.ssafy.match.group.study.dto.response.StudyInfoResponseDto;
import com.ssafy.match.group.study.dto.response.StudySimpleInfoResponseDto;
import com.ssafy.match.group.study.dto.response.StudyTopicResponseDto;
import com.ssafy.match.group.study.entity.CompositeMemberStudy;
import com.ssafy.match.group.study.entity.MemberStudy;
import com.ssafy.match.group.study.entity.Study;
import com.ssafy.match.group.study.entity.StudyApplicationForm;
import com.ssafy.match.group.study.entity.StudyTopic;
import com.ssafy.match.group.study.repository.MemberStudyRepository;
import com.ssafy.match.group.study.repository.StudyApplicationFormRepository;
import com.ssafy.match.group.study.repository.StudyRepository;
import com.ssafy.match.group.study.repository.StudyTopicRepository;
import com.ssafy.match.group.studyboard.board.entity.StudyBoard;
import com.ssafy.match.group.studyboard.board.repository.StudyBoardRepository;
import com.ssafy.match.member.dto.MemberSimpleInfoResponseDto;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.time.LocalDateTime;
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
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final ClubRepository clubRepository;
    private final MemberStudyRepository memberStudyRepository;
    private final MemberClubRepository memberClubRepository;
    private final StudyApplicationFormRepository studyApplicationFormRepository;
    private final DBFileRepository dbFileRepository;
    private final StudyBoardRepository studyBoardRepository;
    private final StudyTopicRepository studyTopicRepository;

    // ????????? ????????? ?????? ??????(???????????? ?????? ??????)
    public StudyInfoForCreateResponseDto getInfoForCreate() {
        return StudyInfoForCreateResponseDto.from(makeClubSimpleInfoResponseDtos(
            findClubInMember(findMember(SecurityUtil.getCurrentMemberId()))));
    }

    // ????????? ??????
    @Transactional
    public StudyInfoResponseDto create(StudyCreateRequestDto dto) {
        validCity(dto.getCity());
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = Study.of(dto, findClub(dto.getClubId()), findDBFile(dto.getUuid()), member);
        studyRepository.save(study);

        makeBasicBoards(study);
        addTopics(study, dto.getTopics());

        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        MemberStudy memberStudy = memberStudyRepository
            .findById(compositeMemberStudy)
            .orElseGet(() -> MemberStudy.builder()
                .compositeMemberStudy(compositeMemberStudy)
                .isActive(true)
                .registerDate(LocalDateTime.now())
                .authority(GroupAuthority.?????????)
                .build());

        study.addMember();
        memberStudyRepository.save(memberStudy);

        return getOneStudy(study.getId());
    }

    // ????????? ??????????????? ?????? ??????
    public StudyInfoForUpdateResponseDto getInfoForUpdateStudy(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        if (!SecurityUtil.getCurrentMemberId().equals(study.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        return StudyInfoForUpdateResponseDto.of(study, getStudyTopics(study),
            makeClubSimpleInfoResponseDtos(findClubInMember(member)));
    }

    // ???????????? ?????? ?????????
    private List<StudyTopicResponseDto> getStudyTopics(Study study) {
        return studyTopicRepository.findAllByStudy(study)
            .stream().map(StudyTopicResponseDto::from).collect(Collectors.toList());
    }

    // ????????? ????????????
    @Transactional
    public StudyInfoResponseDto update(Long studyId, StudyUpdateRequestDto dto) {
        validCity(dto.getCity());
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // ?????? ??????
        checkAuthority(member, study);

        study.update(dto, findClub(dto.getClubId()));
        addTopics(study, dto.getTopics());

        return getOneStudy(studyId);
    }

    // ?????? ?????????
    @Transactional
    public DBFileDto changeCoverPic(Long studyId, String uuid) {
        DBFile coverPic = findDBFile(uuid);
        Study study = findStudy(studyId);
        study.setCoverPic(coverPic);
        return getCoverPicUri(studyId);
    }

    // ?????? ????????? ????????????
    public DBFileDto getCoverPicUri(Long studyId) {
        return DBFileDto.of(findStudy(studyId).getCoverPic());
    }

    // ????????? ????????? ??????
    @Transactional
    public HttpStatus plusViewCount(Long studyId) {
        findStudy(studyId).plusViewCount();
        return HttpStatus.OK;
    }

    // ?????? ??????
    @Transactional
    public HttpStatus changeAuthority(Long studyId, Long memberId, String authority) {
        Study study = findStudy(studyId);
        Member changer = findMember(SecurityUtil.getCurrentMemberId());
        Member member = findMember(memberId);

        MemberStudy ms = memberStudyRepository.findById(
                new CompositeMemberStudy(member, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));

        MemberStudy msChanger = memberStudyRepository.findById(
                new CompositeMemberStudy(changer, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        // ?????? ?????? ????????? ?????? ??????
        // ??????????????? ????????? ????????? ??? ??????
        if (!msChanger.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // ???????????? ????????? ????????????
        if (authority.equals("?????????")) {
            study.setMember(member);
            msChanger.setAuthority(GroupAuthority.?????????);
        }
        ms.setAuthority(GroupAuthority.from(authority));
        return HttpStatus.OK;
    }

    // ???????????? ????????????, ????????? ????????? ????????? ??????
    public void checkAuthority(Member member, Study study) {
        MemberStudy ms = memberStudyRepository.findById(
                new CompositeMemberStudy(member, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        if (!ms.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

    @Transactional
    public HttpStatus delete(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        checkAuthority(member, study);
        // ????????? ?????? ????????????
        List<MemberStudy> memberStudies = memberStudyRepository.findMemberRelationInStudy(study);
        for (MemberStudy mem : memberStudies) {
            mem.deActivation();
        }
        // ????????? Cover ??????
        if (study.getCoverPic() != null) {
            String uuid = study.getCoverPic().getId();
            study.initialCoverPic();
            dbFileRepository.deleteById(uuid);
        }
        // ????????? ?????? ??????
        studyTopicRepository.deleteAllByStudy(study);
        // ????????? ?????????, ?????????, ?????? ?????? ?????? ?????? ??? ??????
        study.deActivation();
        return HttpStatus.OK;
    }

    // ????????? ?????? ??????
    public Page<StudySimpleInfoResponseDto> getAllStudy(Pageable pageable) {
        return studyRepository.findAllStudy(StudyProgressState.FINISH, RecruitmentState.RECRUITMENT,
                PublicScope.PUBLIC, pageable)
            .map(m -> StudySimpleInfoResponseDto.of(m, getStudyTopics(m)));
    }

    // ????????? ?????? ??????
    public StudyInfoResponseDto getOneStudy(Long studyId) {
        Study study = findStudy(studyId);

        // ????????? ??????????????? ????????? ?????? + ??????????????? ?????? ???????????? ???????????? ?????? ??????

        // ????????? ???????????? ??????
        if (!study.getIsActive()) {
            throw new CustomException(ErrorCode.DELETED_STUDY);
        }
        return StudyInfoResponseDto.of(study, getStudyTopics(study),
            findMemberInStudy(study).stream().map(MemberSimpleInfoResponseDto::from).collect(
                Collectors.toList()));
    }

    // ?????? ????????? ?????? ?????? ??????
    public StudySimpleInfoResponseDto getOneSimpleStudy(Long studyId) {
        Study study = findStudy(studyId);
        return StudySimpleInfoResponseDto.of(study, getStudyTopics(study));
    }

    // ??? ???????????? ?????? ??????
    public String getMemberAuthority(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        List<MemberStudy> mss = memberStudyRepository.findMemberRelationInStudy(study);

        String authority = "?????????";
        for (MemberStudy ms : mss) {
            if (ms.getCompositeMemberStudy().getMember().getId().equals(member.getId())) {
                authority = ms.getAuthority().toString();
                break;
            }
        }
        return authority;
    }

    // ????????? ????????? ?????????
    public List<MemberSimpleInfoResponseDto> getMembersInStudy(Long studyId) {
        return memberStudyRepository.findMemberInStudy(findStudy(studyId))
            .stream()
            .map(MemberSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void addTopics(Study study, List<String> topics) {
        studyTopicRepository.deleteAllByStudy(study);
        if (topics.isEmpty()) {
            return;
        }
        for (String topic : topics) {
            studyTopicRepository.save(StudyTopic.of(study, topic));
        }
    }

    @Transactional
    public void addMember(Study study, Member member) {
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);

        // DB??? ?????? ?????? ????????? ????????? ?????? ??????
        MemberStudy memberStudy = memberStudyRepository
            .findById(compositeMemberStudy)
            .orElseGet(() -> MemberStudy.builder()
                .compositeMemberStudy(compositeMemberStudy)
                .registerDate(LocalDateTime.now())
                .authority(GroupAuthority.??????)
                .build());

        memberStudy.activation();
        study.addMember();
        memberStudyRepository.save(memberStudy);
    }

    // ????????? ??????
    @Transactional
    public HttpStatus removeMe(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // ?????? ?????????????????? ??????
        CompositeMemberStudy compositeMemberStudy = new CompositeMemberStudy(member, study);
        MemberStudy memberStudy = memberStudyRepository.findById(compositeMemberStudy)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!memberStudy.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        if (memberStudy.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.HOST_CANNOT_LEAVE);
        }

        memberStudy.deActivation();
        study.removeMember();
        return HttpStatus.OK;
    }

    // ????????? ??????
    @Transactional
    public HttpStatus removeMember(Long studyId, Long memberId) {
        Study study = findStudy(studyId);
        Member remover = findMember(SecurityUtil.getCurrentMemberId());
        Member removed = findMember(memberId);

        MemberStudy removerms = memberStudyRepository.findById(
                new CompositeMemberStudy(remover, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!removerms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        MemberStudy removedms = memberStudyRepository.findById(
                new CompositeMemberStudy(removed, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!removedms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        // ???????????? ??????????????? ?????? ????????? ??????
        if (removerms.getAuthority().equals(GroupAuthority.??????)) {
            throw new CustomException(ErrorCode.COMMON_MEMBER_CANNOT_REMOVE);
        }
        // ???????????? ???????????? ????????? ??? ??????
        if (!removedms.getAuthority().equals(GroupAuthority.??????)) {
            throw new CustomException(ErrorCode.ONLY_CAN_REMOVE_COMMON);
        }

        removedms.deActivation();
        study.removeMember();
        return HttpStatus.OK;
    }

    @Transactional
    public void makeBasicBoards(Study study) {
        studyBoardRepository.save(new StudyBoard("????????????", study));
        studyBoardRepository.save(new StudyBoard("?????????", study));
    }

    public Study findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (Boolean.FALSE.equals(study.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_STUDY);
        }

        return study;
    }

    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (Boolean.FALSE.equals(member.getIs_active())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member;
    }

    public Club findClub(Long clubId) {
        if (clubId == null) {
            return null;
        }
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
    }

    public DBFile findDBFile(String uuid) {
        if (uuid == null) {
            return null;
        }
        return dbFileRepository.findById(uuid)
            .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));
    }

    // ????????? ?????? ?????? ?????????
    public List<Club> findClubInMember(Member member) {
        return memberClubRepository.findClubByMember(member);
    }

    // ?????? ???????????? ?????? ?????? ?????????
    public List<Member> findMemberInStudy(Study study) {
        return memberStudyRepository.findMemberInStudy(study);
    }

    // ?????? ????????? ????????? ????????? ?????????
    public List<Study> studyInMember(Member member) {
        return memberStudyRepository.studyInMember(member);
    }

    public void validCity(String city) {
        if (!Stream.of(GroupCity.values()).map(Enum::name)
            .collect(Collectors.toList()).contains(city)) {
            throw new CustomException(ErrorCode.CITY_NOT_FOUND);
        }
    }

    // ?????? ?????? ??????
    public List<ClubSimpleInfoResponseDto> makeClubSimpleInfoResponseDtos(List<Club> clubs) {
        return clubs.stream()
            .map(ClubSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    // ?????? ?????? ????????? ?????? ????????? ???????????? ??????(?????? ????????? boolean? error?)
    public boolean checkCanApply(Long studyId) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);

        // ?????? ?????? ?????? ?????? (????????? ??????, ??????, ??????, ?????? ????????? ???????????? ?????? ??????)
        if (study.getStudyProgressState().equals(StudyProgressState.FINISH)
            || study.getIsActive().equals(Boolean.FALSE) || study.getRecruitmentState()
            .equals(RecruitmentState.FINISH) || !checkAlreadyJoin(study, member)) {
            return false;
        }
        // ?????? ??????
        CompositeMemberStudy cms = new CompositeMemberStudy(member, study);
        Optional<StudyApplicationForm> form = studyApplicationFormRepository.findById(cms);
        if (form.isPresent()) {
//            throw new CustomException(ErrorCode.ALREADY_APPLY);
            return false;
        }

        return true;
    }

    // ????????? ?????? ?????? ?????? ??????
    public boolean checkAlreadyJoin(Study study, Member member) {
        Optional<MemberStudy> ms = memberStudyRepository.findById(
            new CompositeMemberStudy(member, study));
        if (ms.isPresent() && ms.get().getIsActive()) {
//            throw new CustomException(ErrorCode.ALREADY_JOIN);
            return false;
        }
        return true;
    }

    @Transactional
    public StudyFormInfoResponseDto applyStudy(Long studyId, StudyApplicationRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);

        CompositeMemberStudy cms = new CompositeMemberStudy(member, study);
        StudyApplicationForm studyApplicationForm = StudyApplicationForm.of(dto, cms,
            member.getName());

        return StudyFormInfoResponseDto.from(
            studyApplicationFormRepository.save(studyApplicationForm));
    }

    // ?????? ????????? ????????? ?????? ???????????? ??????
    public List<StudyFormSimpleInfoResponseDto> getAllStudyForm(Long studyId) {
        Study study = findStudy(studyId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        // ?????? ?????? ?????? ??????
        MemberStudy ms = memberStudyRepository.findById(
                new CompositeMemberStudy(member, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }

        // ????????? ???????????? ????????? ????????? ??????
        if (ms.getAuthority().equals(GroupAuthority.??????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }

        return studyApplicationFormRepository.formByStudyId(study)
            .stream()
            .map(StudyFormSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    // ????????? ????????? ?????? ???????????? ????????? ?????? ????????? ????????????
    public StudyFormInfoResponseDto getOneStudyForm(Long studyId, Long memberId) {
        CompositeMemberStudy cms = new CompositeMemberStudy(findMember(memberId),
            findStudy(studyId));

        StudyApplicationForm form = studyApplicationFormRepository.oneFormById(cms)
            .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));

        return StudyFormInfoResponseDto.from(form);
    }

    // ?????? ??????
    @Transactional
    public HttpStatus approval(Long studyId, Long memberId) {
        Study study = findStudy(studyId);
        Member member = findMember(memberId);
        Member approver = findMember(SecurityUtil.getCurrentMemberId());

        if (!checkAlreadyJoin(study, member)) {
            throw new CustomException(ErrorCode.ALREADY_JOIN);
        }

        checkAuthorityApprovalReject(approver, study);

        addMember(study, member);
        reject(studyId, memberId);

        return HttpStatus.OK;
    }

    // ????????? ??????
    @Transactional
    public HttpStatus reject(Long studyId, Long memberId) {
        Member rejector = findMember(SecurityUtil.getCurrentMemberId());
        Study study = findStudy(studyId);
        checkAuthorityApprovalReject(rejector, study);

        studyApplicationFormRepository.delete(
            validStudyApplicationForm(findMember(memberId), study));

        return HttpStatus.OK;
    }

    public StudyApplicationForm validStudyApplicationForm(Member member, Study study) {
        return studyApplicationFormRepository
            .findById(new CompositeMemberStudy(member, study))
            .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));
    }

    public void checkAuthorityApprovalReject(Member member, Study study) {
        // ???????????? ?????? ??????
        MemberStudy ms = memberStudyRepository.findById(
                new CompositeMemberStudy(member, study))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND));
        if (!ms.getIsActive()) {
            throw new CustomException(ErrorCode.MEMBER_STUDY_NOT_FOUND);
        }
        // ????????? ???????????? ??????, ??????, ????????? ????????? ??????
        if (ms.getAuthority().equals(GroupAuthority.??????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }
    }
}
