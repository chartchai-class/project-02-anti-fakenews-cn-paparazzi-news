package com.paparazzi.antifakenews.controller;

import com.paparazzi.antifakenews.dto.*;
import com.paparazzi.antifakenews.model.Comment;
import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.Vote;
import com.paparazzi.antifakenews.model.enums.NewsStatus;
import com.paparazzi.antifakenews.repository.VoteRepository;
import com.paparazzi.antifakenews.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {
    private final NewsService newsService;
    private final VoteRepository voteRepository;

    public NewsController(NewsService newsService, VoteRepository voteRepository) {
        this.newsService = newsService;
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public Page<NewsDto> list(@RequestParam(required = false) String q,
                              @RequestParam(required = false) NewsStatus status,
                              @PageableDefault Pageable pageable) {
        Page<News> page = newsService.search(q, status, pageable);
        return page.map(NewsDto::of);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @PostMapping
    public NewsDto create(@Valid @RequestBody NewsCreateRequest req) {
        return NewsDto.of(newsService.create(req));
    }

    @GetMapping("/{id}")
    public NewsDto get(@PathVariable Long id) {
        return NewsDto.of(newsService.get(id));
    }

    @GetMapping("/{id}/votes")
    public Page<VoteDto> votes(@PathVariable Long id, @PageableDefault Pageable pageable) {
        Page<Vote> page = newsService.listVotes(id, pageable);
        return page.map(VoteDto::of);
    }

    @GetMapping("/{id}/comments")
    public Page<CommentDto> comments(@PathVariable Long id, @PageableDefault Pageable pageable) {
        Page<Comment> page = newsService.listComments(id, pageable);
        return page.map(CommentDto::of);
    }

    @PostMapping("/{id}/votes")
    public VoteDto addVote(@PathVariable Long id, @Valid @RequestBody VoteRequest req) {
        return VoteDto.of(newsService.addVote(id, req));
    }

    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest req) {
        return CommentDto.of(newsService.addComment(id, req));
    }

    @GetMapping("/{id}/summary")
    public VotesSummaryDto summary(@PathVariable Long id) {
        Object[] counts = voteRepository.countFakeAndNotFake(id);
        long fake = counts[0] == null ? 0L : ((Number) counts[0]).longValue();
        long notFake = counts[1] == null ? 0L : ((Number) counts[1]).longValue();
        VotesSummaryDto dto = new VotesSummaryDto();
        dto.setFakeCount(fake);
        dto.setNotFakeCount(notFake);
        dto.setStatus(newsService.get(id).getStatus());
        return dto;
    }
}
