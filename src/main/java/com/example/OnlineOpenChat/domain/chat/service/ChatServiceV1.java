package com.example.OnlineOpenChat.domain.chat.service;


import com.example.OnlineOpenChat.common.exception.CustomException;
import com.example.OnlineOpenChat.common.exception.ErrorCode;
import com.example.OnlineOpenChat.domain.chat.model.Message;
import com.example.OnlineOpenChat.domain.chat.model.Room;
import com.example.OnlineOpenChat.domain.chat.model.RoomMember;
import com.example.OnlineOpenChat.domain.chat.model.request.CreateRoomRequest;
import com.example.OnlineOpenChat.domain.chat.model.response.ChatListResponse;
import com.example.OnlineOpenChat.domain.chat.model.response.CreateRoomResponse;
import com.example.OnlineOpenChat.domain.chat.model.response.JoinedRoomListResponse;
import com.example.OnlineOpenChat.domain.repository.ChatRepository;
import com.example.OnlineOpenChat.domain.repository.RoomMemberRepository;
import com.example.OnlineOpenChat.domain.repository.RoomRepository;
import com.example.OnlineOpenChat.domain.repository.UserRepository;
import com.example.OnlineOpenChat.domain.repository.entity.User;
import com.example.OnlineOpenChat.global.redis.ChatStreamRepository;
import com.example.OnlineOpenChat.security.auth.JWTProvider;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatServiceV1 {

    private final ChatRepository chatRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    /**
     * 최근의 채팅 메시지 조회 (테스트)
     * @param from
     * @param to
     * @return
     */
    public ChatListResponse chatList(String from, String to) {

        // TODO : DB가 아니라 Redis등 채팅 로그를 일시적으로 담아두는 곳에서 채팅 내역을 받아온다.

        /*
        List<Chat> chats = chatRepository.findTop10BySenderOrReceiverOrderByTIDDesc(from, to);

        // Entity -> DTO
        List<Message> res = chats.stream()
                .map(chat -> new Message(chat.getReceiver(), chat.getSender(), chat.getMessage()))
                .collect(Collectors.toList());
         */

        Message mock = Message.builder().to("test2").from("test1").message("test message").build();
        List<Message> messageList = new ArrayList<>();
        messageList.add(mock);

        return new ChatListResponse(messageList);
    }



    private final ChatStreamRepository chatRedisRepository;

    /**
     * Redis에 채팅 메시지 저장
     * @param roomId
     * @param message
     */
    public void saveMessage(Long roomId, Message message) {
        // chatRedisRepository.saveMessage(roomId, message);
    }

    /**
     * 가장 최근의 채팅 메시지 기록들 조회
     * @param roomId
     * @param limit
     * @return
     */
    /*
    public List<Message> getRecentMessages(Long roomId, int limit) {
        // return chatRedisRepository.getRecentMessages(roomId, limit);
    }

     */


    /**
     * 새로운 채팅 방 생성
      * @param authString
     * @param request
     * @return
     */
    @Transactional
    public CreateRoomResponse createRoom(String authString, CreateRoomRequest request) {
        
        try {
            // 1) 새로운 방 생성
            Room room = new Room(request.roomName());

            Room savedRoom = roomRepository.save(room);

            // 2) 방 멤버로 초대된 유저들 추가
            for (String inviteeName : request.participants()) {
                User invitee = userRepository.findByNickname(inviteeName)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));

                RoomMember inviteeMember = userToRoomMember(invitee, savedRoom.getId());
                roomMemberRepository.save(inviteeMember);
            }

            // 3) 생성된 방 정보 반환
            return new CreateRoomResponse(ErrorCode.SUCCESS, request.roomName(), savedRoom.getId());

        } catch (CustomException e) {
            return new CreateRoomResponse(ErrorCode.NOT_EXIST_USER, null, null);
        } catch (Exception e) {
            return new CreateRoomResponse(ErrorCode.INTERNAL_SERVER_ERROR, null, null);
        }

    }

    private RoomMember userToRoomMember(User user, Long roomId) {
        return new RoomMember(roomId, user.getId());
    }

    /**
     * UserId로 참여중인 채팅방 목록 조회
     * @param authString
     * @return
     */
    public JoinedRoomListResponse getJoinedRoomsByUserid(String authString) {
        try {

            String userIdStr = JWTProvider.getUserId(authString);
            Long userId = Long.parseLong(userIdStr);

            List<RoomMember> roomMembers = roomMemberRepository.findByUserId(userId);
            return new JoinedRoomListResponse(ErrorCode.SUCCESS, roomMembers);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, null);
        }

    }
}

