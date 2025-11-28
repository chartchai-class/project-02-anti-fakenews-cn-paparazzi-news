package com.paparazzi.antifakenews.config;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository userRepository,
                           NewsRepository newsRepository,
                           VoteRepository voteRepository,
                           CommentRepository commentRepository,
                           PasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setName("Admin");
                admin.setSurname("User");
                admin.setEmail("admin@local");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);

                User member = new User();
                member.setName("Member");
                member.setSurname("One");
                member.setEmail("member@local");
                member.setPasswordHash(encoder.encode("member123"));
                member.setRole(Role.MEMBER);
                userRepository.save(member);

                for (int i = 1; i <= 5; i++) {
                    User reader = new User();
                    reader.setName("Reader" + i);
                    reader.setSurname("User");
                    reader.setEmail("reader" + i + "@local");
                    reader.setPasswordHash(encoder.encode("reader123"));
                    reader.setRole(Role.USER);
                    userRepository.save(reader);
                }
            }

            if (newsRepository.count() == 0) {
                List<User> users = userRepository.findAll();
                User admin = users.stream().filter(u -> u.getRole() == Role.ADMIN).findFirst().orElse(users.get(0));
                User member = users.stream().filter(u -> u.getRole() == Role.MEMBER).findFirst().orElse(users.get(0));
                Random rnd = new Random(42);
                for (int i = 1; i <= 40; i++) {
                    User author = i % 2 == 0 ? admin : member;
                    News n = new News();
                    n.setTopic("News Topic " + i);
                    n.setShortDetail("Short detail for news " + i + " lorem ipsum");
                    n.setFullDetail("Full detail for news " + i + " lorem ipsum dolor sit amet.");
                    n.setReporterName(author.getName());
                    n.setReportDateTime(Instant.now().minusSeconds(3600L * i));
                    n.setAuthor(author);
                    newsRepository.save(n);
                }

                List<News> allNews = newsRepository.findAll();
                List<User> voters = userRepository.findAll();
                for (News n : allNews) {
                    int votes = 5 + rnd.nextInt(20);
                    for (int j = 0; j < votes; j++) {
                        User voter = voters.get(rnd.nextInt(voters.size()));
                        Vote v = new Vote();
                        v.setNews(n);
                        v.setUser(voter);
                        v.setFake(rnd.nextBoolean());
                        v.setComment("I think " + (v.isFake() ? "fake" : "real") + " because #" + j);
                        voteRepository.save(v);
                    }
                    int comments = 3 + rnd.nextInt(10);
                    for (int k = 0; k < comments; k++) {
                        User commenter = voters.get(rnd.nextInt(voters.size()));
                        Comment c = new Comment();
                        c.setNews(n);
                        c.setUser(commenter);
                        c.setContent("Comment #" + k + " on news " + n.getId());
                        commentRepository.save(c);
                    }
                }
            }
        };
    }
}
