package com.zhidao.demo.forum.controller;

import com.zhidao.demo.forum.model.Post;
import com.zhidao.demo.forum.service.ForumService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping({"/forum","/"})
    public String index(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Post> posts = forumService.listPosts(page, 20);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", page);
        return "forum/index";
    }

    @GetMapping("/forum/posts/{id}")
    public String post(@PathVariable Long id, Model model) {
        Post p = forumService.getPost(id).orElse(null);
        model.addAttribute("post", p);
        model.addAttribute("comments", p != null ? forumService.listComments(id) : null);
        return "forum/post";
    }

    @GetMapping("/forum/create")
    public String create() {
        return "forum/create";
    }
}
