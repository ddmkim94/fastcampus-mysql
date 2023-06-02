package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFollowingMembersUsecase {

    private final MemberReadService memberReadService;
    private final FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId) {
        List<Follow> followings = followReadService.getFollowings(memberId);
        List<Long> followingMemberIds = followings.stream().map(follow -> follow.getToMemberId()).toList();
        return memberReadService.getMembers(followingMemberIds);
    }
}
