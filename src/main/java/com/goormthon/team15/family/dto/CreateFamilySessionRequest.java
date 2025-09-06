package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "가족 세션 생성 요청")
public class CreateFamilySessionRequest {
    
    @Schema(description = "세션 이름", example = "우리 가족의 2025년 다짐", required = true)
    @NotBlank(message = "세션 이름은 필수입니다")
    @Size(min = 1, max = 100, message = "세션 이름은 1자 이상 100자 이하여야 합니다")
    private String name;
    
    @Schema(description = "세션 설명", example = "가족 모두의 새해 다짐을 공유해보세요!")
    @Size(max = 500, message = "세션 설명은 500자 이하여야 합니다")
    private String description;
    
    @Schema(description = "세션 비밀번호 (선택사항)", example = "family2025")
    @Size(min = 4, max = 20, message = "세션 비밀번호는 4자 이상 20자 이하여야 합니다")
    private String sessionPassword;
    
    @Schema(description = "최대 참여자 수", example = "10")
    private Integer maxMembers = 10;
    
    public CreateFamilySessionRequest() {}
    
    public CreateFamilySessionRequest(String name, String description, String sessionPassword, Integer maxMembers) {
        this.name = name;
        this.description = description;
        this.sessionPassword = sessionPassword;
        this.maxMembers = maxMembers;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSessionPassword() {
        return sessionPassword;
    }
    
    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
}
