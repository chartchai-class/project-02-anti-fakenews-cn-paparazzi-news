package com.paparazzi.antifakenews.dto;

import com.paparazzi.antifakenews.model.Comment;

import java.time.Instant;

public class CommentDto {
    private Long id;
    private String content;
    private String userName;
    private Instant createdAt;

    public static CommentDto of(Comment c) {
        CommentDto d = new CommentDto();
        d.setId(c.getId());
        d.setContent(c.getContent());
        d.setUserName(c.getUser().getName() + " " + c.getUser().getSurname());
        d.setCreatedAt(c.getCreatedAt());
        return d;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
