package com.example.chat.service;

import com.example.chat.dto.WebSocketTextMessage;
import com.example.chat.redis.RedisMessageBroker;
import com.example.chat.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class WebSocketSessionManager {

    private static final Logger logger =
            LoggerFactory.getLogger(WebSocketSessionManager.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisMessageBroker redisMessageBroker;
    private final RoomManager roomManager;

    /**
     * userId -> WebSocketSession Set
     * 왜 value가 Set인가?
     * -> 하나의 유저가 여러 디바이스(브라우저 탭 등)에서 접속할 수 있기 때문
     */
    private final ConcurrentMap<Long, Set<WebSocketSession>> userSession = new ConcurrentHashMap<>();

    private static final String SERVER_ROOMS_KEY_PREFIX = "chat:server:rooms";

    public WebSocketSessionManager(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper,
            RedisMessageBroker redisMessageBroker,
            RoomManager roomManager
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.redisMessageBroker = redisMessageBroker;
        this.roomManager = roomManager;
    }

    @PostConstruct
    public void initialize() {
        redisMessageBroker.setLocalMessageHandler(this::sendMessageToLocalRoom);
    }

    /**
     * 유저 세션 추가
     * @param userId
     * @param session
     */
    public void addSession(Long userId, WebSocketSession session) {
        logger.info("Adding session {} to server", userId);

        // userId에 해당하는 세션 Set이 없으면 새로 생성 후 추가
        // userId에 해당하는 세션 Set이 이미 있으면 해당 Set에 세션 추가
        userSession
                .computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    /**
     * 유저 세션 제거
     * @param userId
     * @param session
     */
    public void removeSession(Long userId, WebSocketSession session) {
        Set<WebSocketSession> sessions = userSession.get(userId);
        if (sessions != null) {
            sessions.remove(session);


            if (sessions.isEmpty()) {
                userSession.remove(userId);

                /*
                int totalConnectedUsers = userSession.values().stream()
                        .mapToInt(s -> (int) s.stream().filter(WebSocketSession::isOpen).count())
                        .sum();


                if (totalConnectedUsers == 0) {
                    String serverId = redisMessageBroker.getServerId();
                    String serverRoomKey = SERVER_ROOMS_KEY_PREFIX + serverId;

                    Set<String> subscribedRooms =
                            redisTemplate.opsForSet().members(serverRoomKey);

                    if (subscribedRooms == null) {
                        subscribedRooms = Collections.emptySet();
                    }

                    for (String roomIdStr : subscribedRooms) {
                        try {
                            Long roomId = Long.parseLong(roomIdStr);
                            redisMessageBroker.unsubscribeFromRoom(roomId);
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    redisTemplate.delete(serverRoomKey);
                    logger.info("Removed {} {}", totalConnectedUsers, subscribedRooms);
                }

                 */

            }
        }
    }

    /**
     * 해당 채팅 서버 인스턴스가 특정 채팅방 구독 처리
     * @param userId
     * @param roomId
     */
    public void joinRoom(Long userId, Long roomId) {
        String serverId = redisMessageBroker.getServerId();
        String serverRoomKey = SERVER_ROOMS_KEY_PREFIX + serverId;

        Boolean isMember =
                redisTemplate.opsForSet().isMember(serverRoomKey, roomId.toString());

        boolean wasAlreadySubscribed = Boolean.TRUE.equals(isMember);

        if (!wasAlreadySubscribed) {
            redisMessageBroker.subscribeToRoom(roomId);
        }

        redisTemplate.opsForSet().add(serverRoomKey, roomId.toString());

        logger.info(
                "Joined {} for {} {} to server {}",
                roomId,
                userId,
                serverId,
                serverRoomKey
        );
    }

    /**
     * 특정 채팅방에 속한 유저들에게 메시지 전파
     * @param roomId
     * @param message
     */
    public void sendMessageToLocalRoom(
            Long roomId,
            WebSocketTextMessage message
    ) {
        try {
            String json = objectMapper.writeValueAsString(message);
            
            // roomId에 속한 유저들에게 메시지 전달
            for(WebSocketSession s : roomManager.getSessions(roomId)) {
                // 열려있는 세션 대상으로 메시지 전송
                if (s.isOpen()) {
                    try {
                        // 메시지 전송

                        // TODO : sendMessage 자체는 단순히 동기적 동작이므로 별도의 메시지 큐를 만들고 쓰레드풀을 할당하여 비동기적으로 처리할 필요가 있어보임
                        s.sendMessage(new TextMessage(json));
                        logger.info("Sending message to local room {}", roomId);

                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }

            /*
            userSession.forEach((userId, sessions) -> {

                Set<WebSocketSession> closedSessions = new HashSet<>();

                for (WebSocketSession s : sessions) {
                    if (s.isOpen()) {
                        try {
                            // 메시지 전송

                            s.sendMessage(new TextMessage(json));
                            logger.info("Sending message to local room {}", roomId);

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            closedSessions.add(s);
                        }
                    } else {
                        closedSessions.add(s);
                    }
                }

                if (!closedSessions.isEmpty()) {
                    sessions.removeAll(closedSessions);
                }
            });

             */

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
