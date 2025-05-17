package com.test.test.repository;

import com.test.test.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<OtpCode,Integer> {
    Optional<OtpCode> findFirstByPhoneAndCodeAndUsedFalseOrderByExpiresAtDesc(String phone, String code);
    long countByPhoneAndSentAtAfter(String phone, LocalDateTime after);


    boolean existsByPhoneAndSentAtAfter(String phone, LocalDateTime after);
}
