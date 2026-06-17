package com.vulnerable.voting.service;

import com.vulnerable.voting.dto.AspirantDto;
import com.vulnerable.voting.dto.UserDto;
import com.vulnerable.voting.exception.DatabaseException;
import com.vulnerable.voting.model.Aspirant;
import com.vulnerable.voting.model.Role;
import com.vulnerable.voting.model.UserEntity;
import com.vulnerable.voting.repository.AspirantRepository;
import com.vulnerable.voting.repository.RoleRepository;
import com.vulnerable.voting.repository.UserRepository;
import com.vulnerable.voting.security.JwtUtil;
import com.vulnerable.voting.wrapper.ModelWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AspirantRepository aspirantRepository;
    private JwtUtil jwtUtil;


    public UserEntity saveUser(UserDto userDto, String roleName) throws DatabaseException {
            Role role = roleRepository.findByRole(roleName);
            if(userRepository.existsByEmailAddress(userDto.getEmail()))
                throw new DatabaseException("email already exists");
            UserEntity userEntity = UserEntity.builder()
                    .id(userDto.getId())
                    .role(role)
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .emailAddress(userDto.getEmail())
                    .password(hashPassword(userDto.getPassword()))
                    .build();
            return userRepository.save(userEntity);

    }
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public Aspirant saveAspirant(UserDto userDto, String position) throws DatabaseException {
            Aspirant aspirant = Aspirant.builder()
                    .position(position)
                    .user(saveUser(userDto, "ASPIRANT"))
                    .build();
           return aspirantRepository.save(aspirant);


    }
    public Boolean updateAspirantImage(MultipartFile imageFile, String emailAddress)  {
        try {
            UserEntity user = userRepository.findByEmailAddress(emailAddress);
            user.setImageType(imageFile.getContentType());
            user.setImage(imageFile.getBytes());
            userRepository.save(user);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] getUserImage(long l) {
       return userRepository.findById(l).getImage();
    }

    public UserEntity getUser(long l) {
        return userRepository.findById(l);
    }

    public String verifyPassword(String email, String password){
        log.info("password verification started");
        UserEntity userEntity = userRepository.findByEmailAddress(email);
        if (BCrypt.checkpw(password, userEntity.getPassword())) {
            return jwtUtil.generateToken(userEntity.getEmailAddress(), userEntity.getRole().getRole());
        }
        else {
            throw new RuntimeException("invalid password");
        }
    }

    public Map<String, List<AspirantDto>> getAllAspirant() {
        Map<String, List<AspirantDto>> map = new HashMap<>();
        aspirantRepository.findAll().forEach(aspirant -> {
            AspirantDto aspirantDto = ModelWrapper.mapToAspirantDto(aspirant);
            if(map.containsKey(aspirantDto.getPosition()))
                map.get(aspirantDto.getPosition()).add(aspirantDto);
            else
                map.put(aspirantDto.getPosition(), new ArrayList<>(List.of(aspirantDto)));
        });
        return map;
    }
    public String extractAuthority(String token){
        return jwtUtil.extractAuthority(token);
    }

    public AspirantDto getAspirant(String email){
       return ModelWrapper.mapToAspirantDto(aspirantRepository.findAspirantByUser_EmailAddress(email)) ;
    }

}
