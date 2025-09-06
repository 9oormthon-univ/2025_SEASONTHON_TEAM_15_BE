package com.goormthon.team15.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그아웃 응답")
public class LogoutResponse {
    
    @Schema(description = "응답 메시지", example = "로그아웃이 완료되었습니다")
    private String message;
    
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    
    public LogoutResponse() {}
    
    public LogoutResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
