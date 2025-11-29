package com.paparazzi.antifakenews.dto;

import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.enums.NewsStatus;

import java.time.Instant;

public class NewsDto {
    private Long id;
    private String topic;
    private String shortDetail;
    private String fullDetail;
    private String imageUrl;
    private String reporterName;
    private Instant reportDateTime;
    private NewsStatus status;

    public static NewsDto of(News n) {
        NewsDto d = new NewsDto();
        d.setId(n.getId());
        d.setTopic(n.getTopic());
        d.setShortDetail(n.getShortDetail());
        d.setFullDetail(n.getFullDetail());
        d.setImageUrl(n.getImageUrl());
        d.setReporterName(n.getReporterName());
        d.setReportDateTime(n.getReportDateTime());
        d.setStatus(n.getStatus());
        return d;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getShortDetail() { return shortDetail; }
    public void setShortDetail(String shortDetail) { this.shortDetail = shortDetail; }
    public String getFullDetail() { return fullDetail; }
    public void setFullDetail(String fullDetail) { this.fullDetail = fullDetail; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public Instant getReportDateTime() { return reportDateTime; }
    public void setReportDateTime(Instant reportDateTime) { this.reportDateTime = reportDateTime; }
    public NewsStatus getStatus() { return status; }
    public void setStatus(NewsStatus status) { this.status = status; }
}
