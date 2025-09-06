package com.goormthon.team15.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수정 응답")
public class UpdateUserResponse {
    
    @Schema(description = "응답 메시지", example = "사용자 정보가 성공적으로 수정되었습니다")
    private String message;
    
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    
    @Schema(description = "수정된 사용자 정보")
    private UserProfileResponse user;
    
    public UpdateUserResponse() {}
    
    public UpdateUserResponse(String message, boolean success, UserProfileResponse user) {
        this.message = message;
        this.success = success;
        this.user = user;
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
    
    public UserProfileResponse getUser() {
        return user;
    }
    
    public void setUser(UserProfileResponse user) {
        this.user = user;
    }
}
