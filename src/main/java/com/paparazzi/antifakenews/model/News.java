package com.paparazzi.antifakenews.model;

import com.paparazzi.antifakenews.model.enums.NewsStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String topic;
    @Column(nullable = false, length = 1000)
    private String shortDetail;
    @Column(nullable = false, length = 5000)
    private String fullDetail;
    private String imageUrl;
    @Column(nullable = false)
    private String reporterName;
    @Column(nullable = false)
    private Instant reportDateTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsStatus status = NewsStatus.UNDECIDED;
    @Column(nullable = false)
    private boolean removed = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @PrePersist
    public void prePersist() {
        if (this.reportDateTime == null) this.reportDateTime = Instant.now();
        if (this.status == null) this.status = NewsStatus.UNDECIDED;
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
    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
}
