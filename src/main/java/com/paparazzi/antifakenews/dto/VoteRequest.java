package com.paparazzi.antifakenews.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VoteRequest {
    @NotNull
    private Boolean isFake;
    @Size(max = 2000)
    private String comment;
    private String imageUrl;

    public Boolean getIsFake() { return isFake; }
    public void setIsFake(Boolean isFake) { this.isFake = isFake; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
