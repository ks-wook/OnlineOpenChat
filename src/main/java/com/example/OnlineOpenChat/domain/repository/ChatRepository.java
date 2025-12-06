package com.example.OnlineOpenChat.domain.repository;


import com.example.OnlineOpenChat.domain.repository.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findTop10BySenderOrReceiverOrderByTIDDesc(String sender, String receiver);
}
