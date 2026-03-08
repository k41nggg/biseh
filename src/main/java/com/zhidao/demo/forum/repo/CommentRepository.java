package com.zhidao.demo.forum.repo;

import com.zhidao.demo.forum.model.Comment;
import com.zhidao.demo.forum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
