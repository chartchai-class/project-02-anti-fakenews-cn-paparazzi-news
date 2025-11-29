package com.paparazzi.antifakenews.controller;

import com.paparazzi.antifakenews.service.ModerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {
    private final ModerationService moderationService;
    public AdminController(ModerationService moderationService) { this.moderationService = moderationService; }

    @PostMapping("/news/{id}/remove")
    public ResponseEntity<Void> removeNews(@PathVariable Long id) {
        moderationService.removeNews(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/votes/{id}/remove")
    public ResponseEntity<Void> removeVote(@PathVariable Long id) {
        moderationService.removeVote(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{id}/remove")
    public ResponseEntity<Void> removeComment(@PathVariable Long id) {
        moderationService.removeComment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<Void> promote(@PathVariable Long id) {
        moderationService.promoteToMember(id);
        return ResponseEntity.ok().build();
    }
}
