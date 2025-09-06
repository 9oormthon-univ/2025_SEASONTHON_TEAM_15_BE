package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가족 설정 요청")
public class FamilySetupRequest {
    
    @Schema(description = "가족 별명", example = "조버드네")
    private String familyNickname;
    
    @Schema(description = "사용자 닉네임", example = "코알라")
    private String userNickname;
    
    @Schema(description = "메모지 템플릿 ID", example = "HOUSE_PATTERN")
    private String memoTemplateId;
    
    @Schema(description = "가족 세션 이름", example = "우리 가족의 2025년 다짐")
    private String sessionName;
    
    @Schema(description = "가족 세션 설명", example = "가족 모두의 새해 다짐을 공유해보세요!")
    private String sessionDescription;
    
    public FamilySetupRequest() {}
    
    public FamilySetupRequest(String familyNickname, String userNickname, String memoTemplateId, 
                             String sessionName, String sessionDescription) {
        this.familyNickname = familyNickname;
        this.userNickname = userNickname;
        this.memoTemplateId = memoTemplateId;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
    }
    
    // Getters and Setters
    public String getFamilyNickname() {
        return familyNickname;
    }
    
    public void setFamilyNickname(String familyNickname) {
        this.familyNickname = familyNickname;
    }
    
    public String getUserNickname() {
        return userNickname;
    }
    
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
    
    public String getMemoTemplateId() {
        return memoTemplateId;
    }
    
    public void setMemoTemplateId(String memoTemplateId) {
        this.memoTemplateId = memoTemplateId;
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
}
