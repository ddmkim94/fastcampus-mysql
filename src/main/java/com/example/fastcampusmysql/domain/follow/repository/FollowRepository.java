package com.example.fastcampusmysql.domain.follow.repository;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Follow> ROW_MAPPER =
            (resultSet, rowNum) -> Follow.builder()
                    .id(resultSet.getLong("id"))
                    .fromMemberId(resultSet.getLong("fromMemberId"))
                    .toMemberId(resultSet.getLong("toMemberId"))
                    .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                    .build();

    private final static String TABLE = "Follow";

    public List<Follow> findAllByFromMemberId(Long fromMemberId) {
        String sql = """
                SELECT *
                FROM %s
                WHERE fromMemberId = :fromMemberId
                """.formatted(TABLE);

        MapSqlParameterSource param = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);
        return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
    }

    public List<Follow> findAllByToMemberId(Long toMemberId) {
        String sql = """
                SELECT *
                FROM %s
                WHERE toMemberId = :toMemberId
                """.formatted(TABLE);

        MapSqlParameterSource param = new MapSqlParameterSource().addValue("toMemberId", toMemberId);
        return namedParameterJdbcTemplate.query(sql, param, ROW_MAPPER);
    }


    public Follow save(Follow follow) {
        if (follow.getId() == null) {
            return insert(follow);
        }
        throw new UnsupportedOperationException("Follow는 갱신을 지원하지 않습니다.");
    }

    private Follow insert(Follow follow) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

}
