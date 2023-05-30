package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;

    public Member create(RegisterMemberCommand command) {
        Member member = Member.builder()
                .email(command.email())
                .nickname(command.nickname())
                .birthDay(command.birthDay())
                .build();

        // member save!
        return memberRepository.save(member);
    }
}
