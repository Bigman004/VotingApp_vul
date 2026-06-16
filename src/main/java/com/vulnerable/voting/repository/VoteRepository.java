package com.vulnerable.voting.repository;
import com.vulnerable.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    boolean existsByAspirant_PositionAndUser_Id(String position, Long userId);

    List<Vote> findAllByAspirant_Id(Integer aspirantId);
}
