package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDateTime;

public record RegisterMemberCommand(
        String email,
        String nickname,
        LocalDateTime birthDay) {

}
