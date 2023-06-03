package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
        /*
            select createdDate, memberId, count(id)
            from Post
            where memberId = :memberId and createdDate between firstDate and lastDate
            group by createdDate memberId;
         */
        return postRepository.groupByCreatedDate(request);
    }
}
