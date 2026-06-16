package com.vulnerable.voting.service;

import com.vulnerable.voting.dto.AspirantDto;
import com.vulnerable.voting.dto.VoteDto;
import com.vulnerable.voting.exception.DatabaseException;
import com.vulnerable.voting.model.Aspirant;
import com.vulnerable.voting.model.UserEntity;
import com.vulnerable.voting.model.Vote;
import com.vulnerable.voting.repository.AspirantRepository;
import com.vulnerable.voting.repository.VoteRepository;
import com.vulnerable.voting.security.AuthorizationDetails;
import com.vulnerable.voting.wrapper.ModelWrapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@Slf4j
public class VotingService {
    private final VoteRepository voteRepository;
    private final AspirantRepository aspirantRepository;

    public void vote(Integer aspirantId, UserEntity user) {
        Aspirant aspirant = aspirantRepository.findById(aspirantId).get();
        if(voteRepository.existsByAspirant_PositionAndUser_Id(aspirant.getPosition(), user.getId()))
            throw new DatabaseException("vote already exist");
        voteRepository.save(Vote.builder()
                        .voted(true)
                        .user(user)
                        .createdDate(LocalDateTime.now())
                        .aspirant(aspirant)
                .build());
    }

    public Integer countVote(Integer aspirantId) {
        return voteRepository.findAllByAspirant_Id(aspirantId).size();
    }
}
