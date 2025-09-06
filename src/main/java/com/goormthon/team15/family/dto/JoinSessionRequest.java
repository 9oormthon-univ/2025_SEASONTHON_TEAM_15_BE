package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "가족 세션 참여 요청")
public class JoinSessionRequest {
    
    @Schema(description = "초대 코드", example = "ABC123", required = true)
    @NotBlank(message = "초대 코드는 필수입니다")
    @Size(min = 6, max = 20, message = "초대 코드는 6자 이상 20자 이하여야 합니다")
    private String inviteCode;
    
    @Schema(description = "세션 비밀번호 (세션에 비밀번호가 설정된 경우 필수)", example = "family2025")
    @Size(min = 4, max = 20, message = "세션 비밀번호는 4자 이상 20자 이하여야 합니다")
    private String sessionPassword;
    
    public JoinSessionRequest() {}
    
    public JoinSessionRequest(String inviteCode, String sessionPassword) {
        this.inviteCode = inviteCode;
        this.sessionPassword = sessionPassword;
    }
    
    // Getters and Setters
    public String getInviteCode() {
        return inviteCode;
    }
    
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
    
    public String getSessionPassword() {
        return sessionPassword;
    }
    
    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }
}
