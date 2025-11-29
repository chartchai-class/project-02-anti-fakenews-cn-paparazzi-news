package com.paparazzi.antifakenews.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private boolean removed = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public News getNews() { return news; }
    public void setNews(News news) { this.news = news; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
}
