package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private static final String TABLE = "Members";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static RowMapper<Member> rowMapper = (resultSet, rowNum) -> Member.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthDay(resultSet.getObject("birthDay", LocalDateTime.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();


    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM %s WHERE id = :id".formatted(TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        Member member = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    public List<Member> findAllByIdIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        String sql = """
                SELECT *
                FROM %s
                WHERE id in (:ids)
                """.formatted(TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Member.builder()
                .id(id)
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birthDay(member.getBirthDay())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member) {
        String sql = "update %s set email = :email, nickname = :nickname, birthday = :birthDay where id = :id".formatted(TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, params);
        return member;
    }
}
