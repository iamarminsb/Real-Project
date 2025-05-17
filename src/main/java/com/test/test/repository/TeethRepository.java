package com.test.test.repository;

import com.test.test.entity.EntityVisit;
import com.test.test.entity.Teeth;
import com.test.test.entity.TeethStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeethRepository extends JpaRepository<TeethStatus,Integer> {

    List<TeethStatus> findByEntityVisit(EntityVisit entityVisit);
    void deleteByIdAndEntityVisit(int id, EntityVisit entityVisit);
    Optional<TeethStatus> findByIdAndEntityVisit(int id, EntityVisit entityVisit);
    boolean existsByEntityVisitAndTeethAndNumberOfTeeths(EntityVisit entityVisit, Teeth teeth, int numberOfTeeths);


}
