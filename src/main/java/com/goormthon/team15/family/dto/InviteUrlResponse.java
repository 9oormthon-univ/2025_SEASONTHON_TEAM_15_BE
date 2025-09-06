package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "초대 URL 응답")
public class InviteUrlResponse {
    
    @Schema(description = "초대 URL", example = "https://yourapp.com/invite/ABC123")
    private String inviteUrl;
    
    @Schema(description = "비밀번호 보호 여부", example = "true")
    private Boolean hasPassword;
    
    @Schema(description = "초대 코드", example = "ABC123")
    private String inviteCode;
    
    @Schema(description = "세션 이름", example = "우리 가족의 2025년 다짐")
    private String sessionName;
    
    @Schema(description = "세션 설명", example = "가족 모두의 새해 다짐을 공유해보세요!")
    private String sessionDescription;
    
    @Schema(description = "현재 참여자 수", example = "3")
    private Integer currentMemberCount;
    
    @Schema(description = "최대 참여자 수", example = "10")
    private Integer maxMembers;
    
    public InviteUrlResponse() {}
    
    public InviteUrlResponse(String inviteUrl, Boolean hasPassword, String inviteCode, String sessionName, 
                           String sessionDescription, Integer currentMemberCount, Integer maxMembers) {
        this.inviteUrl = inviteUrl;
        this.hasPassword = hasPassword;
        this.inviteCode = inviteCode;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.currentMemberCount = currentMemberCount;
        this.maxMembers = maxMembers;
    }
    
    // Getters and Setters
    public String getInviteUrl() {
        return inviteUrl;
    }
    
    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
    
    public Boolean getHasPassword() {
        return hasPassword;
    }
    
    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }
    
    public String getInviteCode() {
        return inviteCode;
    }
    
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
    
    public String getSessionName() {
        return sessionName;
    }
    
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    
    public String getSessionDescription() {
        return sessionDescription;
    }
    
    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }
    
    public Integer getCurrentMemberCount() {
        return currentMemberCount;
    }
    
    public void setCurrentMemberCount(Integer currentMemberCount) {
        this.currentMemberCount = currentMemberCount;
    }
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
}
