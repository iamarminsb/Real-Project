package com.test.test.repository;

import com.test.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<User,Integer> {
    Optional<User> findByPhone(String phone);

    User findByUsername(String username);
}
