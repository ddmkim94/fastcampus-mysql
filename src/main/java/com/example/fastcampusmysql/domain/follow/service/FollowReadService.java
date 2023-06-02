package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowRepository followRepository;
}
