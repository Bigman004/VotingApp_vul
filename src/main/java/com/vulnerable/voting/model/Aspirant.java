package com.vulnerable.voting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aspirants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aspirant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String position;
    @OneToOne
    @JoinColumn(name = "email", referencedColumnName = "email_address")
    UserEntity user;

}
