package com.ssafy.match.member.service;

import com.ssafy.match.member.dto.*;
import com.ssafy.match.common.entity.*;
import com.ssafy.match.member.dto.request.*;
import com.ssafy.match.member.dto.response.LoginResponseDto;
import com.ssafy.match.member.entity.EmailCheck;
import com.ssafy.match.member.entity.MemberTechstack;
import com.ssafy.match.member.entity.composite.CompositeMemberTechstack;
import com.ssafy.match.common.repository.*;
import com.ssafy.match.jwt.TokenProvider;
import com.ssafy.match.member.entity.Member;
import com.ssafy.match.common.entity.DetailPosition;
import com.ssafy.match.common.repository.DetailPositionRepository;
import com.ssafy.match.member.repository.EmailCheckRepository;
import com.ssafy.match.member.repository.MemberRepository;
import com.ssafy.match.member.repository.MemberTechstackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TechstackRepository techstackRepository;
    private final DetailPositionRepository detailPositionRepository;
    private final MemberTechstackRepository memberTechstackRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final JavaMailSender javaMailSender;

//    @Value("${spring.mail.username}")
//    private String from;

    @Transactional
    public Long certSignup(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return -1L;
        } else {
            String key = certified_key();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
//            message.setFrom(from);
            message.setSubject("????????? ??????");
            message.setText(key);
            javaMailSender.send(message);
//            Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(email);
//            if(emailCheck.isEmpty()) {
            EmailCheck check = new EmailCheck(email, key, Boolean.FALSE);
            emailCheckRepository.save(check);
//            } else {
//                emailCheck.get().updateKey(key);
//            }
            return check.getId();
        }
    }

    @Transactional
    public Long certPassword(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isPresent()) {
            return -1L;
        } else {
            String key = certified_key();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
//            message.setFrom(from);
            message.setSubject("????????? ??????");
            message.setText(key);
            javaMailSender.send(message);
//            Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(email);
//            if(emailCheck.isEmpty()) {
            EmailCheck check = new EmailCheck(email, key, Boolean.FALSE);
            emailCheckRepository.save(check);
//            } else {
//                emailCheck.get().updateKey(key);
//            }
            return check.getId();
        }
    }

    @Transactional
    public Long emailAuthCode(Long id, EmailCertRequestDto emailCertRequestDto) throws Exception {
//        Optional<EmailCheck> emailCheck = emailCheckRepository.findByEmail(emailCertRequestDto.getEmail());
        EmailCheck emailCheck = emailCheckRepository.findById(id).orElseThrow(() -> new Exception("?????? id??? ???????????? ????????????!"));
        if (emailCheck.getAuthCode().equals(emailCertRequestDto.getAuthCode())) {
            emailCheck.updateIsCheck(Boolean.TRUE);
            return emailCheck.getId();
        }
        return -1L;
    }

    @Transactional(readOnly = true)
    public Boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public HttpStatus changePassword(ForgetChangePasswordRequestDto forgetChangePasswordRequestDto) throws Exception {
        EmailCheck emailCheck = emailCheckRepository.findById(forgetChangePasswordRequestDto.getId()).orElseThrow(() -> new NullPointerException("????????? ????????? ?????? id?????????!"));
        if (!forgetChangePasswordRequestDto.getEmail().equals(emailCheck.getEmail())) {
            throw new Exception("????????? ???????????????! ????????? email??? ????????? ???????????? ????????????!");
        }
        if (emailCheck.getIs_check().equals(Boolean.FALSE)) {
            throw new Exception("email????????? ???????????? ???????????????!");
        }
        Member member = memberRepository.findByEmail(emailCheck.getEmail())
                .orElseThrow(() -> new NullPointerException("?????????????????? ?????? ??????????????????."));
        forgetChangePasswordRequestDto.setPassword(member, passwordEncoder);
        emailCheckRepository.deleteById(forgetChangePasswordRequestDto.getId());
        return HttpStatus.OK;
    }

    @Transactional
    public MemberResponseDto signup(SignupRequestDto signupRequestDto) throws Exception {
        EmailCheck emailCheck = emailCheckRepository.findById(signupRequestDto.getId()).orElseThrow(() -> new NullPointerException("????????? ????????? ?????? id?????????!"));
        if (emailCheck.getIs_check().equals(Boolean.FALSE)) {
            throw new Exception("email????????? ???????????? ???????????????!");
        }
        if (memberRepository.existsByEmail(emailCheck.getEmail())) {
            throw new Exception("?????? ???????????? ?????? ???????????????");
        }
        if (memberRepository.existsByNickname(signupRequestDto.getNickname())) {
            throw new Exception("????????? ????????? ?????????");
        }

        Member member = signupRequestDto.toMember(passwordEncoder, emailCheck.getEmail());
        Member ret = memberRepository.save(member);

        if (signupRequestDto.getDpositionList() != null) {
            addDetailPosition(signupRequestDto.getDpositionList(), ret);
        }
        if (signupRequestDto.getTechList() != null) {
            addTechList(signupRequestDto.getTechList(), ret);
        }
        emailCheckRepository.deleteById(signupRequestDto.getId());
        return MemberResponseDto.of(ret);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws Exception{
        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //???????????? ??????
        Member member = memberRepository.getById(Long.parseLong(authentication.getName()));
        if (!member.getIs_active()) {
            throw new Exception("????????? ???????????????!");
        }
        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken ??????
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        LoginResponseDto loginResponseDto = LoginResponseDto.of(member, tokenDto);
        return loginResponseDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
        }
        // 2. Access Token ?????? Member ID ????????????
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));
        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }
        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // 6. ????????? ?????? ????????????

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        // ?????? ??????
        return tokenDto;
    }

    @Transactional
    public void addDetailPosition(List<String> dPositionList, Member member) {
        for (String dPosition : dPositionList) {
            DetailPosition innerDposition = DetailPosition
                    .builder()
                    .member(member)
                    .name(dPosition)
                    .build();
            detailPositionRepository.save(innerDposition);
        }
    }

    @Transactional
    public void addTechList(HashMap<String, String> techList, Member member) throws Exception {
        for (Map.Entry<String, String> entry : techList.entrySet()) {
            Techstack techstack = techstackRepository.findByName(entry.getKey())
                    .orElseThrow(() -> new NullPointerException("?????? ?????? ????????? ????????????."));
            validLevel(entry.getValue());
            CompositeMemberTechstack compositeMemberTechstack = CompositeMemberTechstack
                    .builder()
                    .member(member)
                    .techstack(techstack)
                    .build();
            MemberTechstack memberTechstack = MemberTechstack.builder().compositeMemberTechstack(compositeMemberTechstack).level(entry.getValue()).build();
            memberTechstackRepository.save(memberTechstack);
        }
    }

    @Transactional(readOnly = true)
    public void validLevel(String level) throws Exception {
        if (!Stream.of(Level.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(level)) {
            throw new Exception("???????????? ?????? level?????????");
        }
    }

    private String certified_key() { //10?????? ????????? ???????????? ????????? ??????
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = random.nextInt(75) + 48; //0<=num<75
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < 10);
        return sb.toString();
    }
}