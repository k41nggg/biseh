package com.zhidao.demo.forum.service;

import com.zhidao.demo.forum.dto.CommentDto;
import com.zhidao.demo.forum.dto.PostDto;
import com.zhidao.demo.forum.model.Comment;
import com.zhidao.demo.forum.model.Post;
import com.zhidao.demo.forum.model.User;
import com.zhidao.demo.forum.repo.CommentRepository;
import com.zhidao.demo.forum.repo.PostRepository;
import com.zhidao.demo.forum.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LocalSummarizer summarizer;

    public ForumService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, LocalSummarizer summarizer) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.summarizer = summarizer;
    }

    public Page<Post> listPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(PostDto dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseGet(() -> {
            User u = new User();
            u.setUsername(dto.getUsername());
            u.setDisplayName(dto.getUsername());
            return userRepository.save(u);
        });
        Post p = new Post();
        p.setTitle(dto.getTitle());
        p.setContent(dto.getContent());
        p.setAuthor(user);
        // generate summary
        String summary = summarizer.summarize(dto.getContent(), 3, 400);
        p.setSummary(summary);
        return postRepository.save(p);
    }

    public Comment addComment(Long postId, CommentDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("帖子未找到"));
        User user = userRepository.findByUsername(dto.getUsername()).orElseGet(() -> {
            User u = new User();
            u.setUsername(dto.getUsername());
            u.setDisplayName(dto.getUsername());
            return userRepository.save(u);
        });
        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(user);
        c.setContent(dto.getContent());
        return commentRepository.save(c);
    }

    public List<Comment> listComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("帖子未找到"));
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    public String summarizePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("帖子未找到"));
        return summarizer.summarize(post.getContent(), 5, 1000);
    }
}
