package com.example.fastcampusmysql.domain.member.entity;

import com.example.fastcampusmysql.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    @Test
    void testChangeName() {
        Member member = MemberFixtureFactory.create();
        String expected = "박은빈";

        member.changeNickName(expected);
        assertEquals(expected, member.getNickname());
    }

    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
    @Test
    void testNickNameMaxLength() {
        Member member = MemberFixtureFactory.create();
        String overLength = "eunbineunbin";

        assertThrows(IllegalArgumentException.class, () -> member.changeNickName(overLength));
    }

}