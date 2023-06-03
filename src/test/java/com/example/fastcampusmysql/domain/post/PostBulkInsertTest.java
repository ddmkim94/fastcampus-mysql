package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void bulkInsert() {
        EasyRandom easyRandom = PostFixtureFactory.get(
                3L,
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 6, 1)
        );

        StopWatch stopWatch = new StopWatch();
        stopWatch.start(); // 스탑워치 시작

        int _1만 = 10000;
        List<Post> posts = IntStream.range(0, _1만 * 100)
                .parallel() // 병렬 연산?
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();

        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();

        postRepository.bulkInsert(posts);

        queryStopWatch.stop();
        System.out.println("db insert time : " + queryStopWatch.getTotalTimeSeconds());
    }
}

