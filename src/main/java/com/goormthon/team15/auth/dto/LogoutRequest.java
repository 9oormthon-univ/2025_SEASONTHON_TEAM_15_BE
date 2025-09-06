package com.goormthon.team15.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그아웃 요청")
public class LogoutRequest {
    
    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiJ9...", required = true)
    @NotBlank(message = "토큰은 필수입니다")
    private String token;
    
    public LogoutRequest() {}
    
    public LogoutRequest(String token) {
        this.token = token;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
