package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.PostLike;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;

    public Long create(Post post, MemberDto memberDto) {
        PostLike postLike = PostLike.builder()
                .memberId(memberDto.id())
                .postId(post.getId())
                .build();

        return postLikeRepository.save(postLike).getPostId();
    }

}
