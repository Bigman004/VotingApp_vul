package com.vulnerable.voting.serviceTest;

import com.vulnerable.voting.dto.AspirantDto;
import com.vulnerable.voting.dto.UserDto;
import com.vulnerable.voting.model.Aspirant;
import com.vulnerable.voting.model.Role;
import com.vulnerable.voting.model.UserEntity;
import com.vulnerable.voting.repository.AspirantRepository;
import com.vulnerable.voting.repository.UserRepository;
import com.vulnerable.voting.repository.RoleRepository;
import com.vulnerable.voting.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private AspirantRepository aspirantRepository;

    private AspirantDto aspirantDto;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        userDto = UserDto.builder()
                .firstName("Ayomide").
                email("adedijiay123@gmail.com").lastName("John").build();
    }

    @Test
    void saveUser_test() {
        Role role = Role.builder().role("VOTER").build();
        Role role2 = Role.builder().role("ASPIRANT").build();
        UserEntity user = UserEntity.builder()
                .lastName(userDto.getLastName())
                .emailAddress(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .build();
        Aspirant asp = Aspirant.builder()
                .position("leader")
                .build();


        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(roleRepository.findByRole("VOTER")).thenReturn(role);
        when(roleRepository.findByRole("ASPIRANT")).thenReturn(role2);
        when(userRepository.existsByEmailAddress(userDto.getEmail())).thenReturn(false);
        when(aspirantRepository.save(any(Aspirant.class))).thenReturn(asp);


        //arrange
        UserEntity userEntity = userService.saveUser(userDto, "VOTER");
        Aspirant aspirant = userService.saveAspirant(userDto, "ASPIRANT");


        Assertions.assertNotNull(userEntity);
        Assertions.assertNotNull(aspirant);

    }
}
