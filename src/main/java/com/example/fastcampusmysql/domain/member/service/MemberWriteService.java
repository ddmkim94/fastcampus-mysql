package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNicknameHistoryRepository memberNickNameHistoryRepository;

    @Transactional
    public Member register(RegisterMemberCommand command) {
        Member member = Member.builder()
                .email(command.email())
                .nickname(command.nickname())
                .birthDay(command.birthDay())
                .build();

        Member savedMember = memberRepository.save(member);
        saveMemberNicknameHistory(savedMember);
        return savedMember;
    }

    @Transactional
    public void changeNickName(Long id, String nickname) {
        /*
            1. 회원 이름을 변경
            2. 변경 내역을 저장
         */
        Member member = memberRepository.findById(id).orElseThrow();
        member.changeNickName(nickname);
        memberRepository.save(member);

        saveMemberNicknameHistory(member);

    }

    private void saveMemberNicknameHistory(Member member) {
        MemberNicknameHistory history = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();

        memberNickNameHistoryRepository.save(history);
    }
}
