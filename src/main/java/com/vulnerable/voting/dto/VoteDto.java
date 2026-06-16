package com.vulnerable.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private int id;
    private boolean voted;
    private Integer aspirantId;
    private Long voteUserId;
    private String position;
}
