package com.goormthon.team15.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 프로필 응답")
public class UserProfileResponse {
    
    @Schema(description = "사용자 ID", example = "1")
    private Long id;
    
    @Schema(description = "사용자명", example = "testuser")
    private String username;
    
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
    
    
    @Schema(description = "세대", example = "TWENTIES")
    private String generation;
    
    @Schema(description = "역할", example = "USER")
    private String role;
    
    @Schema(description = "생성일시", example = "2025-01-01T00:00:00")
    private String createdAt;
    
    @Schema(description = "수정일시", example = "2025-01-01T12:00:00")
    private String updatedAt;
    
    public UserProfileResponse() {}
    
    public UserProfileResponse(Long id, String username, String phoneNumber, 
                              String generation, String role, String createdAt, String updatedAt) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.generation = generation;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    public String getGeneration() {
        return generation;
    }
    
    public void setGeneration(String generation) {
        this.generation = generation;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
