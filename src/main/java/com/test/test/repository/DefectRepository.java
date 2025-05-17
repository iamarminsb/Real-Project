package com.test.test.repository;

import com.test.test.entity.EntityDefect;
import com.test.test.entity.EntityVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DefectRepository extends JpaRepository<EntityDefect,Integer> {
    EntityDefect findFirstByVisitOrderByCreatedAtDesc(EntityVisit visit);
    List<EntityDefect> findAllByVisit(EntityVisit visit);

}
