package com.goormthon.team15.auth.controller;

import com.goormthon.team15.auth.dto.AuthResponse;
import com.goormthon.team15.auth.dto.LoginRequest;
import com.goormthon.team15.auth.dto.LogoutRequest;
import com.goormthon.team15.auth.dto.LogoutResponse;
import com.goormthon.team15.auth.dto.RegisterRequest;
import com.goormthon.team15.auth.service.AuthService;
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
@RequestMapping("/v1/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(
        summary = "회원가입", 
        description = "새로운 사용자를 등록합니다"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n  \"type\": \"Bearer\",\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n\n  \"generation\": \"TWENTIES\",\n  \"role\": \"USER\"\n}"
                )
            ))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "사용자명", required = true, example = "testuser")
            @RequestParam("username") String username,
            @Parameter(description = "전화번호", required = true, example = "010-1234-5678")
            @RequestParam("phoneNumber") String phoneNumber,
            @Parameter(description = "비밀번호", required = true, example = "password123")
            @RequestParam("password") String password,
            @Parameter(description = "세대 (선택사항)", example = "TWENTIES")
            @RequestParam(value = "generation", required = false) String generation) {
        RegisterRequest request = new RegisterRequest(username, phoneNumber, password, generation);
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "로그인", 
        description = "사용자 인증을 수행합니다. 쿼리 파라미터로 사용자명과 비밀번호를 전달하세요."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n  \"type\": \"Bearer\",\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n\n  \"generation\": \"TWENTIES\",\n  \"role\": \"USER\"\n}"
                )
            ))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "사용자명", required = true, example = "testuser")
            @RequestParam("username") String username,
            @Parameter(description = "비밀번호", required = true, example = "password123")
            @RequestParam("password") String password) {
        LoginRequest request = new LoginRequest(username, password);
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "로그아웃", 
        description = "사용자 로그아웃을 수행합니다. 쿼리 파라미터로 JWT 토큰을 전달하세요."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LogoutResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"message\": \"로그아웃이 완료되었습니다\",\n  \"success\": true\n}"
                )
            ))
    })
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @Parameter(description = "JWT 토큰", required = true, example = "eyJhbGciOiJIUzI1NiJ9...")
            @RequestParam("token") String token) {
        LogoutRequest request = new LogoutRequest(token);
        LogoutResponse response = authService.logout(request);
        return ResponseEntity.ok(response);
    }
}
