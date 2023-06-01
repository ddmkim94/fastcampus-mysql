package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {

    private final static Long NAME_MAX_LENGTH = 10L;

    private Long id;
    private String email;
    private String nickname;
    private LocalDateTime birthDay;
    private LocalDateTime createdAt;

    @Builder
    public Member(Long id, String email, String nickname, LocalDateTime birthDay, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.birthDay = Objects.requireNonNull(birthDay);

        validateNickName(nickname);
        this.nickname = Objects.requireNonNull(nickname);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void changeNickName(String nickname) {
        Objects.requireNonNull(nickname);
        validateNickName(nickname);
        this.nickname = nickname;
    }

    private void validateNickName(String nickname) {
        Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH, "최대 길이를 초과했습니다.");
    }

}
