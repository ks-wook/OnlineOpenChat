package com.example.OnlineOpenChat.domain.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long t_id;

    @Column(nullable = false)
    private String name;

    @Column
    private Timestamp create_at;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredentials userCredentials;

    public void setCredentials(UserCredentials credentials) {
        this.userCredentials = credentials;
    }
}
