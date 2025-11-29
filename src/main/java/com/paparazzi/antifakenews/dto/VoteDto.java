package com.paparazzi.antifakenews.dto;

import com.paparazzi.antifakenews.model.Vote;

import java.time.Instant;

public class VoteDto {
    private Long id;
    private boolean isFake;
    private String comment;
    private String imageUrl;
    private String userName;
    private Instant createdAt;

    public static VoteDto of(Vote v) {
        VoteDto d = new VoteDto();
        d.setId(v.getId());
        d.setFake(v.isFake());
        d.setComment(v.getComment());
        d.setImageUrl(v.getImageUrl());
        d.setUserName(v.getUser().getName() + " " + v.getUser().getSurname());
        d.setCreatedAt(v.getCreatedAt());
        return d;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public boolean isFake() { return isFake; }
    public void setFake(boolean fake) { isFake = fake; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
