package com.zhidao.demo.forum.controller;

import com.zhidao.demo.forum.dto.CommentDto;
import com.zhidao.demo.forum.dto.PostDto;
import com.zhidao.demo.forum.model.Comment;
import com.zhidao.demo.forum.model.Post;
import com.zhidao.demo.forum.model.User;
import com.zhidao.demo.forum.service.ForumService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/posts")
@Validated
public class ForumRestController {

    private final ForumService forumService;

    public ForumRestController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping
    public Page<Post> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return forumService.listPosts(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable Long id) {
        return forumService.getPost(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody PostDto dto, HttpSession session) {
        String username = dto.getUsername();
        Object sUser = session.getAttribute("user");
        if (sUser instanceof User) {
            username = ((User) sUser).getUsername();
        }
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "需要用户名（请登录或提供用户名）");
        }
        dto.setUsername(username);
        Post p = forumService.createPost(dto);
        return ResponseEntity.created(URI.create("/api/posts/" + p.getId())).body(p);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> comment(@PathVariable Long id, @Valid @RequestBody CommentDto dto, HttpSession session) {
        Object sUser = session.getAttribute("user");
        if (!(sUser instanceof User)) {
            throw new ResponseStatusException(UNAUTHORIZED, "请先登录后发表评论");
        }
        // use session user only
        String username = ((User) sUser).getUsername();
        dto.setUsername(username);
        try {
            Comment c = forumService.addComment(id, dto);
            return ResponseEntity.ok(c);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{id}/comments")
    public List<Comment> comments(@PathVariable Long id) {
        return forumService.listComments(id);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<String> summary(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.summarizePost(id));
    }
}
