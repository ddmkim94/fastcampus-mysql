package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.PageHelper;
import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "Post";

    private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER =
            (resultSet, rowNum) -> new DailyPostCount(
                    resultSet.getLong("memberId"),
                    resultSet.getObject("createdDate", LocalDate.class),
                    resultSet.getLong("count")
            );

    private static final RowMapper<Post> POST_MAPPER = (resultSet, rowNum) -> Post.builder()
                    .id(resultSet.getLong("id"))
                    .memberId(resultSet.getLong("memberId"))
                    .contents(resultSet.getString("contents"))
                    .createdDate(resultSet.getObject("createdDate", LocalDate.class))
                    .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                    .build();

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        String sql = """
                    SELECT createdDate, memberId, count(id) as count
                    FROM %s
                    WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
                    GROUP BY createdDate, memberId;
                """.formatted(TABLE);

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        String sql = """
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size OFFSET :offset
                """.formatted(TABLE, PageHelper.orderBy(pageable.getSort()));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        List<Post> posts = namedParameterJdbcTemplate.query(sql, params, POST_MAPPER);
        return new PageImpl<>(posts, pageable, getCount(memberId));
    }

    private Long getCount(Long memberId) {
        String sql = """
                SELECT count(id)
                FROM %s
                WHERE memberId = :memberId
                """.formatted(TABLE);

        MapSqlParameterSource param = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            return insert(post);
        }
        throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
    }

    public void bulkInsert(List<Post> posts) {
        String sql = """
                INSERT INTO `%s` (
                memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """.formatted(TABLE);

        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private Post insert(Post post) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
