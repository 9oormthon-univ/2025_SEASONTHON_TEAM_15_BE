package com.goormthon.team15.service;

import com.goormthon.team15.dto.AuthResponse;
import com.goormthon.team15.dto.LoginRequest;
import com.goormthon.team15.dto.RegisterRequest;
import com.goormthon.team15.entity.User;
import com.goormthon.team15.repository.UserRepository;
import com.goormthon.team15.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest registerRequest) {
        // 중복 체크
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("이미 사용 중인 사용자명입니다");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다");
        }
        
        // 새 사용자 생성
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.USER);
        
        User savedUser = userRepository.save(user);
        
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(savedUser);
        
        return new AuthResponse(
            token,
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getRole().name()
        );
    }
    
    public AuthResponse login(LoginRequest loginRequest) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 사용자 정보 조회
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponse(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}
