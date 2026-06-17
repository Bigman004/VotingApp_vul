package com.vulnerable.voting.controller;
import com.vulnerable.voting.dto.UserDto;
import com.vulnerable.voting.exception.DatabaseException;
import com.vulnerable.voting.model.UserEntity;
import com.vulnerable.voting.security.AuthorizationDetails;
import com.vulnerable.voting.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
   private final UserService userService;
   private final AuthorizationDetails authorizationDetails;


   @GetMapping("/save_user")
    public ResponseEntity<?> saveUser(){
       return new ResponseEntity<>(UserDto.builder().build(), HttpStatus.CREATED);
   }
   @PostMapping("/save_user")
    public ResponseEntity<?> saveUser(@RequestPart UserDto userData, @RequestPart(name = "imageFile") MultipartFile imageFile,
                                      @RequestParam(value = "position", defaultValue = "") String position){
       try {
           if(position.isEmpty()){
               userService.saveUser(userData, "VOTER");
               saveImage(imageFile, userData.getEmail());
               return new ResponseEntity<>(HttpStatus.CREATED);

           }
           UserEntity user = userService.saveAspirant(userData, position).getUser();
           saveImage(imageFile, user.getEmailAddress());
           return new ResponseEntity<>(HttpStatus.CREATED);

       }
        catch(Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
   }

    private boolean saveImage(MultipartFile imageFile, String emailAddress){
       try {
           userService.updateAspirantImage(imageFile, emailAddress);
           return true;
       }
       catch(Exception e) {
           System.out.println(e.getMessage());
           return false;
       }
   }
   @GetMapping("/image")
    public ResponseEntity<?> getImage(){
       UserEntity user = userService.getUser(authorizationDetails.getUser().getId());
       return new ResponseEntity<>(user.getImage(), HttpStatus.OK);
   }
   @PostMapping("/image")
   public ResponseEntity<?> uploadImage(@RequestPart("imageFile") MultipartFile imageFile){
       try {
           if(imageFile.isEmpty()){
               return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
           }
           userService.updateAspirantImage(imageFile, authorizationDetails.getUser().getEmailAddress());
           return new ResponseEntity<>(HttpStatus.CREATED);
       }
       catch(Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
   }
   @PostMapping("/login")
    public ResponseEntity<?> login(@RequestPart UserDto user, HttpServletResponse httpServletResponse){

       try {
           String token = userService.verifyPassword(user.getEmail(), user.getPassword());
           Cookie cookie = new Cookie("token", token);
           cookie.setMaxAge(60*60);
           cookie.setHttpOnly(true);
           cookie.setSecure(true);
           cookie.setAttribute("SameSite", "Strict");
           cookie.setPath("/");
           httpServletResponse.addCookie(cookie);
           return new ResponseEntity<>(userService.extractAuthority(token), HttpStatus.OK);
       }
       catch(Exception e) {
           log.info(e.getMessage());
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }
   }
   @GetMapping("/vote_page")
    public ResponseEntity<?> getVotePage1(){

       return new ResponseEntity<>(userService.getAllAspirant(), HttpStatus.OK);
   }
   @GetMapping("/ping")
    public ResponseEntity<?> getPing(){
       return new ResponseEntity<>("pong", HttpStatus.OK);
   }
}
