package com.goormthon.team15.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "메모 응답")
public class MemoResponse {
    
    @Schema(description = "메모 ID", example = "1")
    private Long id;
    
    @Schema(description = "메모 제목", example = "오늘 할 일")
    private String title;
    
    @Schema(description = "메모 내용", example = "1. 운동하기\n2. 책 읽기")
    private String content;
    
    @Schema(description = "작성자 정보")
    private UserInfo user;
    
    @Schema(description = "대상자 정보")
    private UserInfo targetUser;
    
    @Schema(description = "익명 여부", example = "false")
    private Boolean isAnonymous;
    
    @Schema(description = "메모 상태", example = "ACTIVE")
    private String status;
    
    @Schema(description = "메모지 색깔", example = "LIGHT_PINK")
    private String color;
    
    @Schema(description = "생성일시", example = "2025-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2025-01-01T12:00:00")
    private LocalDateTime updatedAt;
    
    public MemoResponse() {}
    
    public MemoResponse(Long id, String title, String content, UserInfo user, UserInfo targetUser, 
                       Boolean isAnonymous, String status, String color, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.targetUser = targetUser;
        this.isAnonymous = isAnonymous;
        this.status = status;
        this.color = color;
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public UserInfo getUser() {
        return user;
    }
    
    public void setUser(UserInfo user) {
        this.user = user;
    }
    
    public UserInfo getTargetUser() {
        return targetUser;
    }
    
    public void setTargetUser(UserInfo targetUser) {
        this.targetUser = targetUser;
    }
    
    public Boolean getIsAnonymous() {
        return isAnonymous;
    }
    
    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Schema(description = "사용자 정보")
    public static class UserInfo {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;
        
        @Schema(description = "사용자명", example = "testuser")
        private String username;
        
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
        
        @Schema(description = "세대", example = "TWENTIES")
        private String generation;
        
        public UserInfo() {}
        
        public UserInfo(Long id, String username, String phoneNumber, String generation) {
            this.id = id;
            this.username = username;
            this.phoneNumber = phoneNumber;
            this.generation = generation;
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
    }
}
