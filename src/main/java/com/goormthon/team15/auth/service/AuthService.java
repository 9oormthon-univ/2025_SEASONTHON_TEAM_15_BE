package com.goormthon.team15.auth.service;

import com.goormthon.team15.auth.dto.AuthResponse;
import com.goormthon.team15.auth.dto.LoginRequest;
import com.goormthon.team15.auth.dto.LogoutRequest;
import com.goormthon.team15.auth.dto.LogoutResponse;
import com.goormthon.team15.auth.dto.RegisterRequest;
import com.goormthon.team15.family.dto.JoinSessionRequest;
import com.goormthon.team15.family.service.FamilySessionService;
import com.goormthon.team15.user.entity.User;
import com.goormthon.team15.common.exception.UserAlreadyExistsException;
import com.goormthon.team15.common.exception.UserNotFoundException;
import com.goormthon.team15.user.repository.UserRepository;
import com.goormthon.team15.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private FamilySessionService familySessionService;
    
    public AuthResponse register(RegisterRequest request) {
        // 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("이미 사용 중인 사용자명입니다");
        }
        
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("이미 사용 중인 전화번호입니다");
        }
        
        
        // 사용자 생성
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // generation 처리
        if (request.getGeneration() != null && !request.getGeneration().isEmpty()) {
            try {
                User.Generation generation = User.Generation.valueOf(request.getGeneration().toUpperCase());
                user.setGeneration(generation);
            } catch (IllegalArgumentException e) {
                // 잘못된 generation 값이면 null로 처리
            }
        }
        
        User savedUser = userRepository.save(user);
        
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(savedUser);
        
        return new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getPhoneNumber(),
            savedUser.getGeneration() != null ? savedUser.getGeneration().name() : null,
            savedUser.getRole().name(),
            savedUser.getMemoColor().name()
        );
    }
    
    public AuthResponse login(LoginRequest request) {
        // 사용자명 또는 전화번호로 사용자 찾기
        User user = userRepository.findByUsername(request.getUsername())
            .orElse(userRepository.findByPhoneNumber(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다")));
        
        try {
            // 인증 (실제 사용자명으로 인증)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
            );
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new org.springframework.security.authentication.BadCredentialsException("사용자명 또는 비밀번호가 올바르지 않습니다");
        }
        
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponse(
            token,
            user.getUsername(),
            user.getPhoneNumber(),
            user.getGeneration() != null ? user.getGeneration().name() : null,
            user.getRole().name(),
            user.getMemoColor().name()
        );
    }
    
    public LogoutResponse logout(LogoutRequest request) {
        // 토큰 유효성 검사
        if (!jwtUtil.validateToken(request.getToken())) {
            return new LogoutResponse("유효하지 않은 토큰입니다", false);
        }
        
        // 토큰에서 사용자명 추출
        String username = jwtUtil.extractUsername(request.getToken());
        
        // 사용자 존재 확인
        if (!userRepository.existsByUsername(username)) {
            return new LogoutResponse("사용자를 찾을 수 없습니다", false);
        }
        
        // 로그아웃 성공 (실제로는 클라이언트에서 토큰을 삭제해야 함)
        return new LogoutResponse("로그아웃이 완료되었습니다", true);
    }
    
    /**
     * 회원가입과 가족 가입을 통합하여 처리
     * 가족 가입에 실패하면 회원가입도 롤백됩니다
     */
    @Transactional
    public AuthResponse registerWithFamilyJoin(RegisterRequest request, String inviteCode, String sessionPassword) {
        // 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("이미 사용 중인 사용자명입니다");
        }
        
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("이미 사용 중인 전화번호입니다");
        }
        
        // 사용자 생성
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // generation 처리
        if (request.getGeneration() != null && !request.getGeneration().isEmpty()) {
            try {
                User.Generation generation = User.Generation.valueOf(request.getGeneration().toUpperCase());
                user.setGeneration(generation);
            } catch (IllegalArgumentException e) {
                // 잘못된 generation 값이면 null로 처리
            }
        }
        
        User savedUser = userRepository.save(user);
        
        // 가족 가입 시도 (실패하면 전체 트랜잭션 롤백)
        if (inviteCode != null && !inviteCode.trim().isEmpty()) {
            try {
                JoinSessionRequest joinRequest = new JoinSessionRequest(inviteCode, sessionPassword);
                familySessionService.joinFamilySession(joinRequest, savedUser);
            } catch (Exception e) {
                // 가족 가입 실패 시 예외를 다시 던져서 트랜잭션 롤백
                throw new RuntimeException("가족 가입에 실패했습니다: " + e.getMessage(), e);
            }
        }
        
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(savedUser);
        
        return new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getPhoneNumber(),
            savedUser.getGeneration() != null ? savedUser.getGeneration().name() : null,
            savedUser.getRole().name(),
            savedUser.getMemoColor().name()
        );
    }
}
