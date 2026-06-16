package com.vulnerable.voting.repository;

import com.vulnerable.voting.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmailAddress(String email);

    UserEntity findById(Long id);

    boolean existsByEmailAddress(String email);
}
