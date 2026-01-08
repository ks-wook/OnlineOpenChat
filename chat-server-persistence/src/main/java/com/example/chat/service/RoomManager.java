package com.example.chat.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface RoomManager {

    void joinRoom(Long roomId, Long userId, WebSocketSession session);

    void joinRooms(Set<Long> roomIds, Long userId, WebSocketSession session);

    void leaveRoom(Long roomId, WebSocketSession session);

    void removeSession(WebSocketSession session);

    Set<WebSocketSession> getSessions(Long roomId);
}
