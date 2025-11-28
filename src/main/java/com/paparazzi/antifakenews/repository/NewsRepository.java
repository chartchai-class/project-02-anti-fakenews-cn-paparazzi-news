package com.paparazzi.antifakenews.repository;

import com.paparazzi.antifakenews.model.News;
import com.paparazzi.antifakenews.model.enums.NewsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n from News n where n.removed=false and (:q is null or lower(n.topic) like lower(concat('%',:q,'%')) or lower(n.shortDetail) like lower(concat('%',:q,'%')) or lower(n.reporterName) like lower(concat('%',:q,'%'))) and (:status is null or n.status=:status)")
    Page<News> search(@Param("q") String q, @Param("status") NewsStatus status, Pageable pageable);
}
