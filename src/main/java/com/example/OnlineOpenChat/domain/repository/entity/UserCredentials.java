package com.example.OnlineOpenChat.domain.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredentials {

    @Id
    @OneToOne
    @JoinColumn(name = "user_t_id")
    private User user;

    @Column(nullable = false)
    private String hashed_password;
}
