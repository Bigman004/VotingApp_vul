package com.vulnerable.voting.repository;

import com.vulnerable.voting.model.Aspirant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AspirantRepository extends JpaRepository<Aspirant, Integer> {
    Aspirant findAspirantByUser_EmailAddress(String email);
}
