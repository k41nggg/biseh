package com.zhidao.demo.forum.dto;

import jakarta.validation.constraints.NotBlank;

public class PostDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String username; // author username (optional when logged-in)

    // getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
