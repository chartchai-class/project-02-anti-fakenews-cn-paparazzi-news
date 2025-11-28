package com.paparazzi.antifakenews.repository;

import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.User;
import com.paparazzi.antifakenews.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Page<Vote> findByNewsIdAndRemovedFalse(Long newsId, Pageable pageable);
    boolean existsByNewsAndUserAndRemovedFalse(News news, User user);
    @Query("select sum(case when v.removed=false and v.isFake=true then 1 else 0 end), sum(case when v.removed=false and v.isFake=false then 1 else 0 end) from Vote v where v.news.id=:newsId")
    Object[] countFakeAndNotFake(@Param("newsId") Long newsId);
}
