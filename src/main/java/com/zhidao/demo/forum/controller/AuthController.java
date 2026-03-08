package com.zhidao.demo.forum.controller;

import com.zhidao.demo.forum.model.User;
import com.zhidao.demo.forum.service.AuthService;
import com.zhidao.demo.forum.service.ForumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        return authService.authenticate(username, password).map(user -> {
            session.setAttribute("user", user);
            return "redirect:/forum";
        }).orElseGet(() -> {
            model.addAttribute("error", "用户名或密码错误");
            return "auth/login";
        });
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String displayName, @RequestParam String password, HttpSession session, Model model) {
        try {
            User u = authService.register(username, displayName, password);
            session.setAttribute("user", u);
            return "redirect:/forum";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/forum";
    }
}
