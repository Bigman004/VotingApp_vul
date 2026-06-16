package com.vulnerable.voting.wrapper;

import com.vulnerable.voting.dto.AspirantDto;
import com.vulnerable.voting.dto.UserDto;
import com.vulnerable.voting.dto.VoteDto;
import com.vulnerable.voting.model.Aspirant;
import com.vulnerable.voting.model.UserEntity;
import com.vulnerable.voting.model.Vote;

public class ModelWrapper {
    public static Vote mapToVote(VoteDto voteDto) {
       return Vote.builder()
                .id(voteDto.getId())
                .voted(voteDto.isVoted())
                .build();
    }

    public static Aspirant mapToAspirant(AspirantDto aspirantDto) {
        return Aspirant.builder()
                .id(aspirantDto.getAspirantId())
                .build();
    }
    public static AspirantDto mapToAspirantDto(Aspirant aspirant) {
        return AspirantDto.builder()
                .aspirantId(aspirant.getId())
                .position(aspirant.getPosition())
                .lastName(aspirant.getUser().getLastName())
                .firstName(aspirant.getUser().getFirstName())
                .image(aspirant.getUser().getImage())
                .email(aspirant.getUser().getEmailAddress())
                .build();

    }
    public static UserEntity mapToUser(UserDto userDto) {
            return UserEntity.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .emailAddress(userDto.getEmail())
                .build();
    }

}
