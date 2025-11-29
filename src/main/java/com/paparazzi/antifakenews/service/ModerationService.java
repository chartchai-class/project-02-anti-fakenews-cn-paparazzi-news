package com.paparazzi.antifakenews.service;

import com.paparazzi.antifakenews.model.Comment;
import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.User;
import com.paparazzi.antifakenews.model.Vote;
import com.paparazzi.antifakenews.model.enums.Role;
import com.paparazzi.antifakenews.repository.CommentRepository;
import com.paparazzi.antifakenews.repository.NewsRepository;
import com.paparazzi.antifakenews.repository.UserRepository;
import com.paparazzi.antifakenews.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModerationService {
    private final NewsRepository newsRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NewsService newsService;

    public ModerationService(NewsRepository newsRepository, VoteRepository voteRepository, CommentRepository commentRepository, UserRepository userRepository, NewsService newsService) {
        this.newsRepository = newsRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.newsService = newsService;
    }

    @Transactional
    public void removeNews(Long id) {
        News n = newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        n.setRemoved(true);
        newsRepository.save(n);
    }

    @Transactional
    public void removeVote(Long id) {
        Vote v = voteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        v.setRemoved(true);
        voteRepository.save(v);
        newsService.get(v.getNews().getId());
    }

    @Transactional
    public void removeComment(Long id) {
        Comment c = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        c.setRemoved(true);
        commentRepository.save(c);
    }

    @Transactional
    public void promoteToMember(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found"));
        u.setRole(Role.MEMBER);
        userRepository.save(u);
    }
}
