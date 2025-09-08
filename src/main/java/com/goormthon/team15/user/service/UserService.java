package com.goormthon.team15.user.service;

import com.goormthon.team15.user.entity.User;
import com.goormthon.team15.common.exception.UserAlreadyExistsException;
import com.goormthon.team15.common.exception.UserNotFoundException;
import com.goormthon.team15.user.repository.UserRepository;
import com.goormthon.team15.user.dto.UpdateUserRequest;
import com.goormthon.team15.user.dto.UpdateUserResponse;
import com.goormthon.team15.user.dto.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        
        return new UserProfileResponse(
            user.getId(),
            user.getUsername(),
            user.getPhoneNumber(),
            user.getGeneration() != null ? user.getGeneration().name() : null,
            user.getRole().name(),
            user.getHasFamily(),
            user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
            user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null
        );
    }
    
    public UpdateUserResponse updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        
        // 전화번호 중복 확인
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new UserAlreadyExistsException("이미 사용 중인 전화번호입니다");
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        // 비밀번호 업데이트
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        // 세대 업데이트
        if (request.getGeneration() != null && !request.getGeneration().isEmpty()) {
            try {
                User.Generation generation = User.Generation.valueOf(request.getGeneration().toUpperCase());
                user.setGeneration(generation);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 세대 값입니다");
            }
        }
        
//        // 메모지 색깔 업데이트
//        if (request.getMemoColor() != null && !request.getMemoColor().isEmpty()) {
//            try {
//                User.MemoColor memoColor = User.MemoColor.valueOf(request.getMemoColor().toUpperCase());
//                user.setMemoColor(memoColor);
//            } catch (IllegalArgumentException e) {
//                throw new IllegalArgumentException("올바르지 않은 메모지 색깔 값입니다");
//            }
//        }
        
        User updatedUser = userRepository.save(user);
        
        UserProfileResponse userProfile = new UserProfileResponse(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getPhoneNumber(),
            updatedUser.getGeneration() != null ? updatedUser.getGeneration().name() : null,
            updatedUser.getRole().name(),
            updatedUser.getHasFamily(),
            updatedUser.getCreatedAt() != null ? updatedUser.getCreatedAt().toString() : null,
            updatedUser.getUpdatedAt() != null ? updatedUser.getUpdatedAt().toString() : null
        );
        
        return new UpdateUserResponse("사용자 정보가 성공적으로 수정되었습니다", true, userProfile);
    }
}
