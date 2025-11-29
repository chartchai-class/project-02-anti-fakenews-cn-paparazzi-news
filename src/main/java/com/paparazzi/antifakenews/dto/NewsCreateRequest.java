package com.paparazzi.antifakenews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewsCreateRequest {
    @NotBlank
    private String topic;
    @NotBlank
    @Size(max = 1000)
    private String shortDetail;
    @NotBlank
    @Size(max = 5000)
    private String fullDetail;
    private String reporterName;
    private String imageUrl;

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getShortDetail() { return shortDetail; }
    public void setShortDetail(String shortDetail) { this.shortDetail = shortDetail; }
    public String getFullDetail() { return fullDetail; }
    public void setFullDetail(String fullDetail) { this.fullDetail = fullDetail; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
