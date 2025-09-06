package com.goormthon.team15.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 응답")
public class AuthResponse {
    
    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    
    @Schema(description = "토큰 타입", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "사용자명", example = "testuser")
    private String username;
    
    @Schema(description = "이메일", example = "test@example.com")
    private String email;
    
    @Schema(description = "사용자 역할", example = "USER")
    private String role;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, String username, String email, String role) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
