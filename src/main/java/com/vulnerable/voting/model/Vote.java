package com.vulnerable.voting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="votes")
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "aspirant_id")
    private Aspirant aspirant;
    private Boolean voted;
    private LocalDateTime createdDate;
    @ManyToOne
    @JoinColumn(name="user_email", referencedColumnName = "email_address")
    private UserEntity user;  //owner of vote

}
