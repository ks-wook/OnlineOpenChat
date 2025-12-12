package com.example.OnlineOpenChat.domain.user.service;

import com.example.OnlineOpenChat.common.exception.CustomException;
import com.example.OnlineOpenChat.common.exception.ErrorCode;
import com.example.OnlineOpenChat.domain.repository.UserRepository;
import com.example.OnlineOpenChat.domain.repository.entity.User;
import com.example.OnlineOpenChat.domain.user.model.Friend;
import com.example.OnlineOpenChat.domain.user.model.FriendDto;
import com.example.OnlineOpenChat.domain.user.model.request.AddFriendRequest;
import com.example.OnlineOpenChat.domain.user.model.response.AddFriendResponse;
import com.example.OnlineOpenChat.domain.user.model.response.GetFriendListResponse;
import com.example.OnlineOpenChat.domain.user.model.response.UserSearchResponse;
import com.example.OnlineOpenChat.domain.user.repository.FriendRepository;
import com.example.OnlineOpenChat.security.auth.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceV1 {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    /**
     * 유저 검색 처리
     * @param nickname
     * @param myName
     * @return
     */
    public final UserSearchResponse searchUser(String nickname, String myName) {

        try {
            List<String> userList = userRepository.findNameByNicknameMatch(nickname, myName);

            return new UserSearchResponse(ErrorCode.SUCCESS, userList);
        } catch (CustomException e) {
            return new UserSearchResponse(e.getErrorCode(), null);
        } catch (Exception e) {
            return new UserSearchResponse(ErrorCode.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * 친구 추가 처리
     * @param authString
     * @param request
     * @return
     */
    public AddFriendResponse addFriend(String authString, AddFriendRequest request) {

        try {
            Long userId = JWTProvider.getUserIdAsLong(authString);

            // 닉네임을 통해 존재하는 유저인지 검색
            Optional<User> user = userRepository.findByNickname(request.friendNickname());

            if(user.isPresent()) {
                Friend newFriend = new Friend(userId, user.get().getId());
                friendRepository.save(newFriend);
            } else {
                throw new CustomException(ErrorCode.NOT_EXIST_USER, "존재하지 않는 유저입니다.");
            }

            // 친구 추가 성공
            return new AddFriendResponse(ErrorCode.SUCCESS);
        } catch (CustomException e) {
            return new AddFriendResponse(e.getErrorCode());
        } catch (Exception e) {
            return new AddFriendResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
    
    /**
     * 친구 목록 조회
     * @param authString
     * @return
     */
    public GetFriendListResponse getFriendListByUserId(String authString) {
        try {
            Long userId = JWTProvider.getUserIdAsLong(authString);
            List<FriendDto> friendList = friendRepository.findFriendDto(userId);

            return new GetFriendListResponse(ErrorCode.SUCCESS, friendList);

        } catch (CustomException e) {
            return new GetFriendListResponse(e.getErrorCode(), null);
        } catch (Exception e) {
            return new GetFriendListResponse(ErrorCode.INTERNAL_SERVER_ERROR, null);
        }

    }
}
