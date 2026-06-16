package com.vulnerable.voting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.el.stream.Stream;
import org.aspectj.apache.bcel.util.ByteSequence;
import org.hibernate.annotations.Type;

import java.awt.*;
import java.io.OutputStream;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="email_address", unique = true, nullable = false)
    private String emailAddress;
    private String password;
    private String firstName;
    private String lastName;
    private String imageType;

    @Lob
    private byte[] image;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            schema = "public"
    )
    private Role role;
}
