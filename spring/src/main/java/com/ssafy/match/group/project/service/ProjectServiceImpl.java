package com.ssafy.match.group.project.service;

import com.ssafy.match.common.entity.GroupAuthority;
import com.ssafy.match.common.entity.GroupCity;
import com.ssafy.match.common.entity.Level;
import com.ssafy.match.common.entity.ProjectProgressState;
import com.ssafy.match.common.entity.PublicScope;
import com.ssafy.match.common.entity.RecruitmentState;
import com.ssafy.match.common.entity.Techstack;
import com.ssafy.match.common.exception.CustomException;
import com.ssafy.match.common.exception.ErrorCode;
import com.ssafy.match.common.repository.TechstackRepository;
import com.ssafy.match.file.dto.DBFileDto;
import com.ssafy.match.file.entity.DBFile;
import com.ssafy.match.file.repository.DBFileRepository;
import com.ssafy.match.group.club.dto.response.ClubSimpleInfoResponseDto;
import com.ssafy.match.group.club.entity.Club;
import com.ssafy.match.group.club.repository.ClubRepository;
import com.ssafy.match.group.club.repository.MemberClubRepository;
import com.ssafy.match.group.project.dto.request.ProjectApplicationRequestDto;
import com.ssafy.match.group.project.dto.request.ProjectCreateRequestDto;
import com.ssafy.match.group.project.dto.request.ProjectUpdateRequestDto;
import com.ssafy.match.group.project.dto.response.ProjectFormInfoResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectFormSimpleInfoResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectInfoForCreateResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectInfoForUpdateResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectInfoResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectMemberResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectSimpleInfoResponseDto;
import com.ssafy.match.group.project.dto.response.ProjectTechstackResponseDto;
import com.ssafy.match.group.project.entity.CompositeMemberProject;
import com.ssafy.match.group.project.entity.CompositeProjectTechstack;
import com.ssafy.match.group.project.entity.MemberProject;
import com.ssafy.match.group.project.entity.Project;
import com.ssafy.match.group.project.entity.ProjectApplicationForm;
import com.ssafy.match.group.project.entity.ProjectTechstack;
import com.ssafy.match.group.project.repository.MemberProjectRepository;
import com.ssafy.match.group.project.repository.ProjectApplicationFormRepository;
import com.ssafy.match.group.project.repository.ProjectRepository;
import com.ssafy.match.group.project.repository.ProjectTechstackRepository;
import com.ssafy.match.group.projectboard.board.entity.ProjectBoard;
import com.ssafy.match.group.projectboard.board.repository.ProjectBoardRepository;
import com.ssafy.match.member.dto.MemberSimpleInfoResponseDto;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.HashMap;
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
public class ProjectServiceImpl implements ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ClubRepository clubRepository;
    private final MemberClubRepository memberClubRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectApplicationFormRepository projectApplicationFormRepository;
    private final TechstackRepository techstackRepository;
    private final ProjectTechstackRepository projectTechstackRepository;
    private final DBFileRepository dbFileRepository;
    private final ProjectBoardRepository projectBoardRepository;

    // ???????????? ????????? ?????? ??????(????????? ?????? ?????????)
    public ProjectInfoForCreateResponseDto getInfoForCreate() {
        return ProjectInfoForCreateResponseDto.from(makeClubSimpleInfoResponseDtos(
            memberClubRepository.findClubByMember(findMember(SecurityUtil.getCurrentMemberId()))));
    }

    // ???????????? ??????
    @Transactional
    public ProjectInfoResponseDto create(ProjectCreateRequestDto dto) {
        validCity(dto.getCity());
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Project project = new Project(dto, findClub(dto.getClubId()), findDBFile(dto.getUuid()),
            member);
        projectRepository.save(project);

        makeBasicBoards(project); // ?????? ????????????, ????????? ??????
        addTechstack(project, dto.getTechstacks());

        // ??????????????? ????????? ?????? ??????
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        MemberProject memberProject = MemberProject.builder()
            .compositeMemberProject(compositeMemberProject)
            .role(dto.getHostPosition())
            .registerDate(LocalDateTime.now())
            .authority(GroupAuthority.?????????)
            .isActive(true)
            .build();
        project.addRole(dto.getHostPosition());
        memberProjectRepository.save(memberProject);

        return ProjectInfoResponseDto.of(project, projectTechstackFull(project),
            memberRole(project, "?????????"), memberRole(project, "?????????"), memberRole(project, "????????????"),
            "?????????");
    }

    // ???????????? ??????????????? ?????? ??????
    public ProjectInfoForUpdateResponseDto getInfoForUpdateProject(Long projectId) {
        Project project = findProject(projectId);

        if (!SecurityUtil.getCurrentMemberId().equals(project.getMember().getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }

        return ProjectInfoForUpdateResponseDto.of(project,
            projectTechstackSimple(project), makeClubSimpleInfoResponseDtos(
                memberClubRepository.findClubByMember(
                    findMember(SecurityUtil.getCurrentMemberId()))));
    }

    public MemberProject findMemberProject(Member member, Project project) {
        return memberProjectRepository.findMemberProjectByCompositeMemberProject(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
    }

    // ???????????? ????????????
    @Transactional
    public ProjectInfoResponseDto update(Long projectId, ProjectUpdateRequestDto dto) {
        validCity(dto.getCity());
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // ?????? ??????
        getProjectAuthority(member, project);

        project.update(dto, findClub(dto.getClubId()));
        addTechstack(project, dto.getTechstacks());

        return getOneProject(projectId);
    }
    // ?????? ?????????
    @Transactional
    public DBFileDto changeCoverPic(Long projectId, String uuid){
        DBFile coverPic = findDBFile(uuid);
        Project project = findProject(projectId);
        project.setCoverPic(coverPic);
        return getCoverPicUri(projectId);
    }
    // ?????? ????????? ????????????
    public DBFileDto getCoverPicUri(Long projectId) {
        return DBFileDto.of(findProject(projectId).getCoverPic());
    }

    public void getProjectAuthority(Member member, Project project){
        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (!mp.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
    }

    // ???????????? ??????
    @Transactional
    public HttpStatus delete(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // ?????? ??????
        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if (!mp.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // ???????????? ?????? ????????????
        List<MemberProject> memberProjects = memberProjectRepository.findMemberRelationInProject(
            project);
        for (MemberProject mem : memberProjects) {
            mem.deactivation();
        }
        // ???????????? Cover ??????
//        if (project.getCoverPic().getId() != null) {
//            dbFileRepository.delete(project.getCoverPic());
//        }
        // ???????????? ?????? ?????? ?????? (???????????? ??????????)
        projectTechstackRepository.deleteAllByProject(project);
        // ???????????? ????????????
        project.setIsActive(Boolean.FALSE);

        return HttpStatus.OK;
    }

    // ?????? ???????????? ?????? ??????  + (?????? ?????? ??????)
    public Page<ProjectSimpleInfoResponseDto> getAllProject(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllProject(ProjectProgressState.FINISH,
            RecruitmentState.RECRUITMENT, PublicScope.PUBLIC, pageable);
        return projects.map(m -> ProjectSimpleInfoResponseDto.of(m, projectTechstackSimple(m)));
    }

    // ?????? ???????????? ??????
//    public List<ProjectSimpleInfoResponseDto> getRecommendationProject(Pageable pageable) {
////        Pageable limit = PageRequest.of(0, 10);
//        List<Project> projects = projectRepository.findTop10OrderByCreateDateDesc(pageable);
//
//        List<ProjectSimpleInfoResponseDto> projectInfoResponseDtos = new ArrayList<>();
//        for (Project project : projects) {
//            projectInfoResponseDtos.add(
//                ProjectSimpleInfoResponseDto.of(project, projectTechstackSimple(project)));
//        }
//        return projectInfoResponseDtos;
//    }

    // ?????? ???????????? ?????? ??????
    public ProjectInfoResponseDto getOneProject(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        List<MemberProject> mps = memberProjectRepository.findMemberRelationInProject(project);

        String authority = "?????????";
        for (MemberProject mp : mps) {
            if (mp.getCompositeMemberProject().getMember().getId().equals(member.getId())) {
                authority = mp.getAuthority().toString();
                break;
            }
        }
        return ProjectInfoResponseDto.of(project, projectTechstackFull(project),
            memberRole(project, "?????????"), memberRole(project, "?????????"), memberRole(project, "????????????"),
            authority);
    }
    // ??? ???????????? ?????? ??????
    public String getMemberAuthority(Long projectId){
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        List<MemberProject> mps = memberProjectRepository.findMemberRelationInProject(project);

        String authority = "?????????";
        for (MemberProject mp : mps) {
            if (mp.getCompositeMemberProject().getMember().getId().equals(member.getId())) {
                authority = mp.getAuthority().toString();
                break;
            }
        }
        return authority;
    }


    // ?????? ???????????? ?????? ?????? ??????
    public ProjectSimpleInfoResponseDto getOneSimpleProject(Long projectId) {
        Project project = findProject(projectId);
        return ProjectSimpleInfoResponseDto.of(project, projectTechstackSimple(project));
    }

    // ???????????? ?????? ?????? ????????? ??????
    public List<ProjectTechstackResponseDto> projectTechstackSimple(Project project) {
        return projectTechstackRepository.findProjectTechstackByProject(project)
            .stream()
            .map(ProjectTechstackResponseDto::simple)
            .collect(Collectors.toList());
    }

    // ???????????? ?????? ?????? ?????? ??????
    public List<ProjectTechstackResponseDto> projectTechstackFull(Project project) {
        return projectTechstackRepository.findProjectTechstackByProject(project)
            .stream()
            .map(ProjectTechstackResponseDto::full)
            .collect(Collectors.toList());
    }

    // ?????? ???????????? ????????? ????????? ??????
    public List<ProjectMemberResponseDto> memberInProject(Long projectId) {
        return memberProjectRepository.findMemberRelationInProject(findProject(projectId))
            .stream()
            .map(ProjectMemberResponseDto::from)
            .collect(Collectors.toList());
    }

    // ?????? ??????????????? ?????? ????????? ?????? ?????? ?????? ?????????
    public List<MemberSimpleInfoResponseDto> memberRole(Project project, String role) {
        return memberProjectRepository.findMemberRole(project, role)
            .stream()
            .map(MemberSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    // ???????????? ????????? ??????
    @Transactional
    public HttpStatus plusViewCount(Long projectId){
        findProject(projectId).plusViewCount();
        return HttpStatus.OK;
    }

    // ?????? ?????? ??????
    @Transactional
    public void addTechstack(Project project, HashMap<String, String> techstacks) {
        projectTechstackRepository.deleteAllByProject(project);
        if (techstacks == null) {
            return;
        }

        for (String tech : techstacks.keySet()) {
            Techstack techstack = findTechstack(tech);
            Level level = Level.from(techstacks.get(tech));

            CompositeProjectTechstack compositeProjectTechstack = new CompositeProjectTechstack(
                techstack, project);

            projectTechstackRepository.save(new ProjectTechstack(compositeProjectTechstack, level));
        }
    }

    // ?????? ??????
    @Transactional
    public void addMember(Project project, Member member, String role) {
        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        // DB??? ?????? ?????? ????????? ????????? ?????? ??????
        MemberProject memberProject = memberProjectRepository
            .findById(compositeMemberProject)
            .orElseGet(() -> MemberProject.builder()
                .compositeMemberProject(compositeMemberProject)
                .build());

        project.addRole(role);
        memberProject.setRole(role);
        memberProject.setRegisterDate(LocalDateTime.now());
        memberProject.setAuthority(GroupAuthority.??????);
        memberProject.activation();
        memberProjectRepository.save(memberProject);
    }

    // ?????? ??????
    @Transactional
    public HttpStatus removeMe(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());

        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (mp.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.HOST_CANNOT_LEAVE);
        }

        CompositeMemberProject compositeMemberProject = new CompositeMemberProject(member, project);
        // DB??? ?????? ?????? ????????? ????????? ?????? ??????
        MemberProject memberProject = memberProjectRepository.findById(compositeMemberProject)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        project.removeRole(memberProject.getRole());
        memberProject.deactivation();
        return HttpStatus.OK;
    }

    // ?????? ??????
    @Transactional
    public HttpStatus removeMember(Long projectId, Long memberId) {
        Project project = findProject(projectId);
        Member remover = findMember(SecurityUtil.getCurrentMemberId());
        Member removed = findMember(memberId);

        MemberProject removermp = memberProjectRepository.findById(
                new CompositeMemberProject(remover, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        MemberProject removedmp = memberProjectRepository.findById(
                new CompositeMemberProject(removed, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        if (removermp.getAuthority().equals(GroupAuthority.??????)) { // ???????????? ??????????????? ?????? ????????? ??????
            throw new CustomException(ErrorCode.COMMON_MEMBER_CANNOT_REMOVE);
        }

        if (!removedmp.getAuthority().equals(GroupAuthority.??????)) { // ???????????? ???????????? ????????? ??? ??????
            throw new CustomException(ErrorCode.ONLY_CAN_REMOVE_COMMON);
        }

        project.removeRole(removedmp.getRole()); // ???????????? ????????? ?????? ??? ??????
        removedmp.deactivation(); // ????????????
        return HttpStatus.OK;
    }

    // ?????? ??????
    @Transactional
    public HttpStatus changeRole(Long projectId, Long memberId, String role) {
        Project project = findProject(projectId);
        Member member = findMember(memberId);

        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        // ?????? ?????? ????????? ?????? ??????
        // ???????????? ?????? ????????? ????????? ?????? ????????? ?????? ?????? ??????
        // ???????????? ?????? ????????? ????????? ?????? ????????? ?????? ?????? ??????
        // ????????? ????????? ????????? ??? ??????

        project.removeRole(mp.getRole());
        mp.setRole(role);
        project.addRole((role));
        return HttpStatus.OK;
    }

    // ?????? ??????
    @Transactional
    public HttpStatus changeAuthority(Long projectId, Long memberId, String authority) {
        Project project = findProject(projectId);
        Member changer = findMember(SecurityUtil.getCurrentMemberId());
        Member member = findMember(memberId);

        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));

        MemberProject mpChanger = memberProjectRepository.findById(
                new CompositeMemberProject(changer, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        // ?????? ?????? ????????? ?????? ??????
        // ??????????????? ????????? ????????? ??? ??????
        if (!mpChanger.getAuthority().equals(GroupAuthority.?????????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHANGE);
        }
        // ???????????? ????????? ????????????
        if (authority.equals("?????????")) {
            project.setMember(member);
            mpChanger.setAuthority(GroupAuthority.?????????);
        }
        mp.setAuthority(GroupAuthority.from(authority));
        return HttpStatus.OK;
    }

    // ???????????? ??????
    public Project findProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        if (Boolean.FALSE.equals(project.getIsActive())) {
            throw new CustomException(ErrorCode.DELETED_PROJECT);
        }

        return project;
    }

    // ?????? ??????
    public Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (Boolean.FALSE.equals(member.getIs_active())) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member;
    }

    // ?????? ?????? ??????
    public Techstack findTechstack(String techName) {
        return techstackRepository.findByName(techName)
            .orElseThrow(() -> new CustomException(ErrorCode.TECHSTACK_NOT_FOUND));
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

    public void validCity(String city) {
        if (!Stream.of(GroupCity.values()).map(Enum::name)
            .collect(Collectors.toList()).contains(city)) {
            throw new CustomException(ErrorCode.CITY_NOT_FOUND);
        }
    }

    public List<ClubSimpleInfoResponseDto> makeClubSimpleInfoResponseDtos(List<Club> clubs) {
        return clubs.stream()
            .map(ClubSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    public List<MemberSimpleInfoResponseDto> makeMemberSimpleInfoResponseDtos(
        List<Member> members) {
        return members.stream()
            .map(MemberSimpleInfoResponseDto::from)
            .collect(Collectors.toList());
    }

    // ?????? ????????? ??????
    @Transactional
    public void makeBasicBoards(Project project) {
        projectBoardRepository.save(new ProjectBoard("????????????", project));
        projectBoardRepository.save(new ProjectBoard("?????????", project));
    }

    // ?????? ?????? ????????? ?????? ?????? ??? ?????? ??????
    public HttpStatus checkCanApply(Long projectId) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Project project = findProject(projectId);
        // ?????? ?????? ?????? ??????
        if (project.getProjectProgressState().equals(ProjectProgressState.FINISH)
            || project.getIsActive().equals(Boolean.FALSE) || project.getRecruitmentState()
            .equals(RecruitmentState.FINISH)) {
            throw new CustomException(ErrorCode.CANNOT_APPLY);
        }
        checkAlreadyJoin(project, member);

        return HttpStatus.OK;
    }

    // ???????????? ?????? ?????? ?????? ??????
    public void checkAlreadyJoin(Project project, Member member) {
        Optional<MemberProject> mp = memberProjectRepository.findMemberProjectByCompositeMemberProject(
            new CompositeMemberProject(member, project));
        if (mp.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_JOIN);
        }
    }

    @Transactional
    public ProjectFormInfoResponseDto applyProject(Long projectId, ProjectApplicationRequestDto dto) {
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        Project project = findProject(projectId);
        checkAlreadyJoin(project, member);
        CompositeMemberProject cmp = new CompositeMemberProject(member, project);

        Optional<ProjectApplicationForm> form = projectApplicationFormRepository.findById(cmp);
        if (form.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_APPLY);
        }

        ProjectApplicationForm projectApplicationForm = ProjectApplicationForm.of(dto, cmp,
            member.getName());
        return ProjectFormInfoResponseDto.from(projectApplicationFormRepository.save(projectApplicationForm));
    }

    // ?????? ????????? ????????? ?????? ???????????? ??????
    public List<ProjectFormSimpleInfoResponseDto> allProjectForm(Long projectId) {
        Project project = findProject(projectId);
        Member member = findMember(SecurityUtil.getCurrentMemberId());
        // ?????? ?????? ?????? ??????
        MemberProject mp = memberProjectRepository.findById(
                new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_PROJECT_NOT_FOUND));
        if (mp.getAuthority().equals(GroupAuthority.??????)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SELECT);
        }
        return projectApplicationFormRepository.formByProjectId(project)
            .stream()
            .map(ProjectFormSimpleInfoResponseDto::from)
            .collect(Collectors.toList());

    }

    // ????????? ????????? ?????? ???????????? ????????? ?????? ????????? ???????????? (????????? ????????? ?????? ???????????? ??? ??????)
    public ProjectFormInfoResponseDto oneProjectForm(Long projectId, Long memberId) {
        CompositeMemberProject cmp = new CompositeMemberProject(findMember(memberId),
            findProject(projectId));

        ProjectApplicationForm form = projectApplicationFormRepository.oneFormById(cmp)
            .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));

        return ProjectFormInfoResponseDto.from(form);
    }

    // ?????? ??????
    @Transactional
    public HttpStatus approval(Long projectId, Long memberId) {
        Project project = findProject(projectId);
        Member member = findMember(memberId);

        checkAlreadyJoin(project, member);
        ProjectApplicationForm form = validProjectApplicationForm(member, project);

        addMember(project, member, form.getRole());
        reject(projectId, memberId);
        return HttpStatus.OK;
    }

    // ????????? ??????
    @Transactional
    public HttpStatus reject(Long projectId, Long memberId) {
        projectApplicationFormRepository.delete(
            validProjectApplicationForm(findMember(memberId), findProject(projectId)));
        return HttpStatus.OK;
    }

    public ProjectApplicationForm validProjectApplicationForm(Member member, Project project) {
        return projectApplicationFormRepository
            .findById(new CompositeMemberProject(member, project))
            .orElseThrow(() -> new CustomException(ErrorCode.APPLIY_FORM_NOT_FOUND));
    }
}
