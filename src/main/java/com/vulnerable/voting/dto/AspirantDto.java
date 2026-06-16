package com.vulnerable.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AspirantDto {
    private int aspirantId;
    private String position;
    private String email;
    private String firstName;
    private String lastName;
    private byte[] image;
    private int voteCount;
}
