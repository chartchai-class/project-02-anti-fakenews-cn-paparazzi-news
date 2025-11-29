package com.paparazzi.antifakenews.service;

import com.paparazzi.antifakenews.dto.CommentRequest;
import com.paparazzi.antifakenews.dto.NewsCreateRequest;
import com.paparazzi.antifakenews.dto.VoteRequest;
import com.paparazzi.antifakenews.model.Comment;
import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.User;
import com.paparazzi.antifakenews.model.Vote;
import com.paparazzi.antifakenews.model.enums.NewsStatus;
import com.paparazzi.antifakenews.model.enums.Role;
import com.paparazzi.antifakenews.repository.CommentRepository;
import com.paparazzi.antifakenews.repository.NewsRepository;
import com.paparazzi.antifakenews.repository.UserRepository;
import com.paparazzi.antifakenews.repository.VoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public NewsService(NewsRepository newsRepository, VoteRepository voteRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public News create(NewsCreateRequest req) {
        User current = currentUser();
        if (current.getRole() != Role.ADMIN && current.getRole() != Role.MEMBER) throw new SecurityException("not allowed");
        News n = new News();
        n.setTopic(req.getTopic());
        n.setShortDetail(req.getShortDetail());
        n.setFullDetail(req.getFullDetail());
        n.setReporterName(req.getReporterName() != null ? req.getReporterName() : current.getName() + " " + current.getSurname());
        n.setImageUrl(req.getImageUrl());
        n.setAuthor(current);
        return newsRepository.save(n);
    }

    public Page<News> search(String q, NewsStatus status, Pageable pageable) {
        return newsRepository.search(q, status, pageable);
    }

    public News get(Long id) {
        News n = newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        if (n.isRemoved() && currentUser().getRole() != Role.ADMIN) throw new SecurityException("removed");
        recalcStatus(id, n);
        return n;
    }

    @Transactional
    public Vote addVote(Long newsId, VoteRequest req) {
        News n = newsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("not found"));
        if (n.isRemoved()) throw new SecurityException("removed");
        User u = currentUser();
        if (voteRepository.existsByNewsAndUserAndRemovedFalse(n, u)) throw new IllegalArgumentException("already voted");
        Vote v = new Vote();
        v.setNews(n);
        v.setUser(u);
        v.setFake(Boolean.TRUE.equals(req.getIsFake()));
        v.setComment(req.getComment());
        v.setImageUrl(req.getImageUrl());
        Vote saved = voteRepository.save(v);
        recalcStatus(newsId, n);
        return saved;
    }

    @Transactional
    public Comment addComment(Long newsId, CommentRequest req) {
        News n = newsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("not found"));
        if (n.isRemoved()) throw new SecurityException("removed");
        Comment c = new Comment();
        c.setNews(n);
        c.setUser(currentUser());
        c.setContent(req.getContent());
        return commentRepository.save(c);
    }

    public Page<Vote> listVotes(Long newsId, Pageable pageable) {
        return voteRepository.findByNewsIdAndRemovedFalse(newsId, pageable);
    }

    public Page<Comment> listComments(Long newsId, Pageable pageable) {
        return commentRepository.findByNewsIdAndRemovedFalse(newsId, pageable);
    }

    private void recalcStatus(Long newsId, News n) {
        Object[] counts = voteRepository.countFakeAndNotFake(newsId);
        long fake = counts[0] == null ? 0L : ((Number) counts[0]).longValue();
        long notFake = counts[1] == null ? 0L : ((Number) counts[1]).longValue();
        NewsStatus status = fake == notFake ? NewsStatus.UNDECIDED : (fake > notFake ? NewsStatus.FAKE : NewsStatus.NOT_FAKE);
        if (n.getStatus() != status) {
            n.setStatus(status);
            newsRepository.save(n);
        }
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
