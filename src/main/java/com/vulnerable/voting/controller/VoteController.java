package com.vulnerable.voting.controller;


import com.vulnerable.voting.dto.AspirantDto;
import com.vulnerable.voting.dto.VoteDto;
import com.vulnerable.voting.repository.UserRepository;
import com.vulnerable.voting.security.AuthorizationDetails;
import com.vulnerable.voting.service.UserService;
import com.vulnerable.voting.service.VotingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/voteApi")
@AllArgsConstructor
public class VoteController {

    private final UserService userService;
    private final VotingService votingService;
    private final AuthorizationDetails authorizationDetails;

    @GetMapping("/votePage")
    public ResponseEntity<?> getVotePage(){
        System.out.println("voter: " + authorizationDetails.getUser().getEmailAddress());

        return new ResponseEntity<>(userService.getAllAspirant(), HttpStatus.ACCEPTED);
    }
    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestParam(value = "aspirantId", defaultValue = "13") Integer aspirantId){
        try{
            System.out.println("voter: " + authorizationDetails.getUser().getEmailAddress() + " voting: " + aspirantId);
            votingService.vote(aspirantId, authorizationDetails.getUser());
        }
        catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/aspirant")
    public ResponseEntity<?> getAspirant(){
        AspirantDto aspirant = userService.getAspirant(
                authorizationDetails.getUser().getEmailAddress());
        aspirant.setVoteCount(votingService.countVote(aspirant.getAspirantId()));
        return new ResponseEntity<>(aspirant
                , HttpStatus.OK);
    }
    @GetMapping("/total")
    public ResponseEntity<?> getTotal(@RequestParam Integer aspirantId){
        if(!authorizationDetails.getUser().getRole()
                .getRole().equals("ASPIRANT")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(votingService.countVote(aspirantId), HttpStatus.OK);
    }

}
