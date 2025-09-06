package com.goormthon.team15.controller;

import com.goormthon.team15.dto.AuthResponse;
import com.goormthon.team15.dto.LoginRequest;
import com.goormthon.team15.dto.RegisterRequest;
import com.goormthon.team15.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Parameter(description = "사용자명", example = "testuser", required = true)
            @RequestParam String username,
            @Parameter(description = "이메일 주소", example = "test@example.com", required = true)
            @RequestParam String email,
            @Parameter(description = "비밀번호", example = "password123", required = true)
            @RequestParam String password) {
        try {
            RegisterRequest registerRequest = new RegisterRequest(username, email, password);
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "사용자 인증을 수행합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "사용자명", example = "testuser", required = true)
            @RequestParam String username,
            @Parameter(description = "비밀번호", example = "password123", required = true)
            @RequestParam String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @Operation(summary = "인증 테스트", description = "인증이 필요한 엔드포인트 테스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테스트 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("인증 테스트 성공!");
    }
}
