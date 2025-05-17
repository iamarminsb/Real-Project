package com.test.test.repository;

import com.test.test.entity.EntityVisit;
import com.test.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<EntityVisit,Integer> {

    Optional<EntityVisit> findById(int integer);
    List<EntityVisit> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
    long countByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
    Optional<EntityVisit> findFirstByUserOrderByCreatedAtDesc(User user);
    EntityVisit findByUser(User user);
    boolean existsByUserAndIsActiveTrue(User user);
    List<EntityVisit> findByUserOrderByCreatedAtDesc(User user);
    Optional<EntityVisit> findByIdAndUser(int id, User user);



}
